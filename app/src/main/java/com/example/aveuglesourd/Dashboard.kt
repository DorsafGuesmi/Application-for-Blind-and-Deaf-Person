package com.example.aveuglesourd


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import com.example.aveuglesourd.Aveugle.RappelActivity


class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


}

    fun voix(view: View) {
        startActivity(Intent(this,SpeechtoText::class.java))
    }

    fun ajout(view: View) {
        startActivity(Intent(this,Main2Activity::class.java))
    }

    fun chat(view: View) {
        startActivity(Intent(this,SignInActivity::class.java))
    }

    fun notiy(view: View) {
        startActivity(Intent(this, RappelActivitySourd::class.java))
    }

    fun texttospeech(view: View) {

    }
    fun deconnexion(view: View) {

    }

    fun jouer(view: View) {}


}