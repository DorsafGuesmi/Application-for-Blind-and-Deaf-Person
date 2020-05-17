package com.example.aveuglesourd.Aveugle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aveuglesourd.R
import kotlinx.android.synthetic.main.activity_dashboard_aveugle.*

class DashboardAveugle : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_aveugle)

        btnDocument.setOnClickListener{
            startActivity(Intent(this, DocumentActivity::class.java))
            finish()
        }

        btnGallery.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
            finish()
        }

        btnObject.setOnClickListener {
            startActivity(Intent(this, LabelActivity::class.java))
            finish()
        }

        btnRappel.setOnClickListener {
            startActivity(Intent(this, RappelActivity::class.java))
            finish()
        }
    }
}
