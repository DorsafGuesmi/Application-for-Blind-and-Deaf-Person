package com.example.aveuglesourd

import android.app.Activity
import android.content.Intent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aveuglesourd.Util.FirestoreUtil
import com.example.aveuglesourd.service.MyFirebaseInstanceIDService
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_in.*

import org.jetbrains.anko.clearTask
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1
// on va enregistrer des email
    private val signInProviders =
    listOf(AuthUI.IdpConfig.EmailBuilder()
            .setAllowNewAccounts(true)
            .setRequireName(true)
            .build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
 //click sur le bouton
        account_sign_in.setOnClickListener {
            // rediriger vers création user dans le firebase
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setLogo(R.drawable.arrows)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val progressDialog = indeterminateProgressDialog("Créer votre compte")
//initialisation de user ceer une data classe(classe contient les données de user) dans package dans model
                FirestoreUtil.initCurrentUserIfFirstTime {
                    // on peut revenir au connexion aprés que vous aver terminer
                    startActivity(intentFor<communication>().newTask().clearTask())

                    val registrationToken = FirebaseInstanceId.getInstance().token
                    MyFirebaseInstanceIDService.addTokenToFirestore(registrationToken)

                    progressDialog.dismiss()
                }
            }
        }
    }}