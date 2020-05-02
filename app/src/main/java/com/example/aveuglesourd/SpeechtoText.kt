package com.example.aveuglesourd

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_speechto_text.*
import java.lang.Exception
import java.util.*

class SpeechtoText : AppCompatActivity() {
    val REQUEST_CODE_SPEECH_INPUT=100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speechto_text)


//button click to show SpeechToText dialog
                record.setOnClickListener {
                    speak()
                }



            }

            private fun speak() {
                //intent to show SpeechToText dialog
                val mIntent= Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Bonjour dire une chose")
                try {
                    //if there is no error show SpeechToText dialog
                    startActivityForResult(mIntent,REQUEST_CODE_SPEECH_INPUT)
                }
                catch (e: Exception){
                    //if there is an error get error message and show in toast
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                    when(requestCode){
                        REQUEST_CODE_SPEECH_INPUT -> {
                            if(resultCode==  Activity.RESULT_OK && null != data) {
                                //get text from result
                                val result =
                                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                                //Set text to textview
                                textTV.text = result[0]
                            }}}}}