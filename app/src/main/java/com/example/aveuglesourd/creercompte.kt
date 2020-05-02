package com.example.aveuglesourd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authentification_sourd.button
import android.graphics.drawable.BitmapDrawable
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_creercompte.*
import java.util.*

class creercompte : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    companion object {
        val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creercompte)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        button.setOnClickListener {
            sign_up()
        }

        profile_image.setOnClickListener {
            Log.d("creercompte", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d("creercompte", "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            profile_image.setImageBitmap(bitmap)

            profile_image.alpha = 0f

            val bitmapDrawable = BitmapDrawable(bitmap)
            profile_image.setBackgroundDrawable(bitmapDrawable)
        }
    }


    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("creercompte", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("creercompte", "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("creercompte", "Failed to upload image to storage: ${it.message}")
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, editText.text.toString(), profileImageUrl)

        ref.setValue(user)



            .addOnSuccessListener {
                Log.d("creercompte", "Finally we saved the user to Firebase Database")
            }

    }
    class User(val uid: String, val username: String, val profileImageUrl: String)
    fun sign_up() {
        if (editText.text.toString().isEmpty()) {
            editText.error = "entrez votre nom s'il vous plait"
            editText.requestFocus()
            return
        }
        if (editText2.text.toString().isEmpty()) {
            editText2.error = "entrez votre adresse email s'il vous plait"
            editText2.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(editText2.toString()).matches()) {
            editText2.error = "entrez une adresse email valid"
            editText2.requestFocus()
            return
        }
        if (editText3.text.toString().isEmpty()) {
            editText3.error = "entrez votre mot de passe s'il vous plait"
            editText3.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(editText2.text.toString(), editText3.text.toString())
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                startActivity(Intent(this, authentification_sourd::class.java))
                                finish()
                            }
                        }

                } else {

                    Toast.makeText(
                        baseContext, "Authentication est echoué,ressayez.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                // ...
            }
    }
}



