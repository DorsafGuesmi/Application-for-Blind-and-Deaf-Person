package com.example.aveuglesourd.Aveugle

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.widget.TextView
import com.example.aveuglesourd.R
import kotlinx.android.synthetic.main.activity_alarm_on.*
import java.util.*

class AlarmOnActivity : AppCompatActivity() {


    lateinit var mTTS: TextToSpeech
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_on)

        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.ENGLISH
            }
        })

        val tvMedicament = findViewById<TextView>(R.id.tvM)

        var intent = intent
        val Nom = intent.getStringExtra("Nom")

        tvMedicament.setText("C'est le temp de votre médicament : "+Nom)

        var mp = MediaPlayer.create(applicationContext, R.raw.alarm_clock_2015_new)
        mp.start()

        btnStop.setOnClickListener(){
            i++

            val handler = Handler()
            handler.postDelayed({
                if (i == 1) {
                    mTTS.speak("double click on this button to stop alarm", TextToSpeech.QUEUE_FLUSH, null)
                } else if (i == 2) {
                    mp.stop()
                }
                i = 0
            }, 500)
        }

    }
}
