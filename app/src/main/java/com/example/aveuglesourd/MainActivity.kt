package com.example.aveuglesourd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }




    fun aveuglee(view: View) {

        startActivity(Intent(this,splashaveugle::class.java))
    }
    fun sourdd(view: View) {
        startActivity(Intent(this,splashsourd::class.java))

    }



}
