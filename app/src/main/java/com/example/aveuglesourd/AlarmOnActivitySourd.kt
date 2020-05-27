package com.example.aveuglesourd

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_alarm_on_sourd.*

class AlarmOnActivitySourd : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_on_sourd)

        var intent = intent
        val Nom = intent.getStringExtra("Nom")
        var text: String = ""
        text+= "C'est le temp de votre médicament : $Nom\n"

        tvM.text = text
        var mp = MediaPlayer.create(applicationContext, R.raw.alarm_clock_2015_new)
        mp.start()

        btnStop.setOnClickListener(){
                    mp.stop()
                    finish()
        }

    }
}
