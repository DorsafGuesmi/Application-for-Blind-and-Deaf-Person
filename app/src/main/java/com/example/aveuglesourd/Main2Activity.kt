package com.example.aveuglesourd

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler

import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso


class Main2Activity : AppCompatActivity() {
    private var chooseImage: Button? = null
    private var btnUploadImage: Button? = null
    private var viewGallery: TextView? = null
    private var imgPreview: ImageView? = null
    private var imgDescription: EditText? = null
    private var uploadProgress: ProgressBar? = null
    private var imgUrl: Uri? = null
    private var mStorageRef: StorageReference? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploadTask: StorageTask<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        uploadProgress = findViewById(R.id.uploadProgress)
        chooseImage = findViewById(R.id.chooseImage)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        viewGallery = findViewById(R.id.viewGallery)
        imgDescription = findViewById(R.id.imgDescription)
        imgPreview = findViewById(R.id.imgPreview)
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")



    }
    fun viewGallery(view: View) {val intent = Intent(this@Main2Activity, ViewImageActivity::class.java)
        startActivity(intent)}
    fun btnUploadImage(view: View) {  if (mUploadTask != null && mUploadTask!!.isInProgress) {
        Toast.makeText(this@Main2Activity, "Upload in progress", Toast.LENGTH_LONG)
            .show()
    } else {
        uploadImage()
    }}
    fun chooseImage(view: View) { showFileChoose()}

    private fun showFileChoose() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,CHOOSE_IMAGE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imgUrl = data.data
            Picasso.get().load(imgUrl).into(imgPreview)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadImage() {
        if (imgUrl != null) {
            val fileReference = mStorageRef!!.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(imgUrl!!)
            )
            mUploadTask = fileReference.putFile(imgUrl!!)
                .addOnSuccessListener {
                    val handler = Handler()
                    handler.postDelayed({ uploadProgress!!.progress = 0 }, 500)
                    fileReference.downloadUrl
                        .addOnSuccessListener { uri ->
                            val upload = Upload(
                                imgDescription!!.text.toString().trim { it <= ' ' },
                                uri.toString()
                            )
                            val uploadID = mDatabaseRef!!.push().key
                            mDatabaseRef!!.child(uploadID!!).setValue(upload)
                            Toast.makeText(
                                this@Main2Activity,
                                "Upload successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            imgPreview!!.setImageResource(R.drawable.upp)
                            imgDescription!!.setText("")
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this@Main2Activity,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    uploadProgress!!.progress = progress.toInt()
                }
        } else {
            Toast.makeText(this@Main2Activity, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val CHOOSE_IMAGE = 1
    }




}