package com.example.aveuglesourd.Aveugle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import com.example.aveuglesourd.R
import kotlinx.android.synthetic.main.activity_dashboard_aveugle.*
import java.util.*

class DashboardAveugle : AppCompatActivity() {

    internal var i = 0
    lateinit var mTTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_aveugle)

        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.ENGLISH
            }
        })

        btnDocument.setOnClickListener{
            i++
            val handler = Handler()
            handler.postDelayed({
                if (i == 1) {
                    mTTS.speak("if you want to read a document, please double click this button", TextToSpeech.QUEUE_FLUSH, null)
                } else if (i == 2) {
                    startActivity(Intent(this, DocumentActivity::class.java))
                }
                i = 0
            }, 500)

        }

        btnGallery.setOnClickListener {
            i++

            val handler = Handler()
            handler.postDelayed({
                if (i == 1) {
                    mTTS.speak("if you want to access your gallery, please double click this button", TextToSpeech.QUEUE_FLUSH, null)
                } else if (i == 2) {
                    startActivity(Intent(this, GalleryActivity::class.java))
                }
                i = 0
            }, 500)
        }

        btnObject.setOnClickListener {

            i++

            val handler = Handler()
            handler.postDelayed({
                if (i == 1) {
                    mTTS.speak("if you want to recognize an object, please double click this button", TextToSpeech.QUEUE_FLUSH, null)
                } else if (i == 2) {
                    startActivity(Intent(this, LabelActivity::class.java))
                }
                i = 0
            }, 500)
        }

        btnRappel.setOnClickListener {

            i++

            val handler = Handler()
            handler.postDelayed({
                if (i == 1) {
                    mTTS.speak("if you want to set our alarm to remind you when to drink your medicine, please double click this button",
                        TextToSpeech.QUEUE_FLUSH, null)
                } else if (i == 2) {
                    startActivity(Intent(this, RappelActivity::class.java))
                }
                i = 0
            }, 500)
        }
    }
}
