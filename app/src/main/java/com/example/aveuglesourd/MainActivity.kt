package com.example.aveuglesourd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import com.example.aveuglesourd.Aveugle.splashaveugle
import java.util.*

class MainActivity : AppCompatActivity() {

    internal var i = 0
    lateinit var mTTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.ENGLISH
            }
        })
    }

    fun aveuglee(view: View) {

        i++

        val handler = Handler()
        handler.postDelayed({
            if (i == 1) {
                mTTS.speak("if you are a blind person, please double click this button", TextToSpeech.QUEUE_FLUSH, null)
            } else if (i == 2) {
                startActivity(Intent(this, splashaveugle::class.java))
            }
            i = 0
        }, 500)

    }
    fun sourdd(view: View) {

        i++

        val handler = Handler()
        handler.postDelayed({
            if (i == 1) {
                mTTS.speak("deaf specific button", TextToSpeech.QUEUE_FLUSH, null)
                Toast.makeText(this, "please double click this button", Toast.LENGTH_SHORT).show()
            } else if (i == 2) {
                startActivity(Intent(this,splashsourd::class.java))
            }
            i = 0
        }, 500)


    }
}
