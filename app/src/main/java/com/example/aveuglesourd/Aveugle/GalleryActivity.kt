package com.example.aveuglesourd.Aveugle

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.aveuglesourd.R
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector
import kotlinx.android.synthetic.main.activity_gallery.*
import java.util.*

class GalleryActivity : AppCompatActivity() {

    lateinit var btGallery: Button
    lateinit var imageViewGallery: ImageView
    lateinit var mTTS: TextToSpeech
    private var i = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        init()
    }

    private fun init() {
        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.UK
            }
        })

        btGallery = findViewById(R.id.btGallery)
        imageViewGallery = findViewById(R.id.image_View_Gallery)
        btGallery.setOnClickListener {
            i++

            val handler = Handler()
            handler.postDelayed({
                if (i == 1) {
                    mTTS.speak("double click on this button to open gallery", TextToSpeech.QUEUE_FLUSH, null)
                } else if (i == 2) {
                    onClick()
                }
                i = 0
            }, 500)
        }

    }

    private fun onClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var intent: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        } else {
            var intent: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                var selectedImage: Uri? = data!!.getData()
                managedImageFromUri(selectedImage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun managedImageFromUri(selectedImage: Uri?) {
        var bitmap : Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImage)
            imageViewGallery.setImageBitmap(bitmap)
            onDeviceRecognizeText(bitmap,selectedImage)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun onDeviceRecognizeText(
        bitmap: Bitmap,
        selectedImage: Uri?
    ) {
        var text: String = ""
        var image: FirebaseVisionImage = FirebaseVisionImage.fromFilePath(this, selectedImage!!)
        var detector: FirebaseVisionTextDetector = FirebaseVision.getInstance().visionTextDetector
        var result: Task<FirebaseVisionText> = detector.detectInImage(image)
            .addOnSuccessListener { p0 ->
                for (block: FirebaseVisionText.Block in p0.blocks) {
                    var boundingBox: Rect = block.boundingBox!!
                    var cornerPoints: Array<Point> = block.cornerPoints!!
                    //text += block.text
                    for(line: FirebaseVisionText.Line in block.lines){
                        text+= line.text + "\n"
                    }
                }
                tvOutputGallery.setText(text)
                textToSpeech(tvOutputGallery)
            }
            .addOnFailureListener { p0 ->
                Toast.makeText(this@GalleryActivity, p0.message, Toast.LENGTH_LONG).show() }
    }

    private fun textToSpeech(tvOutputGallery: TextView) {
        val toSpeak = tvOutputGallery.text.toString()
        if (toSpeak == "") {
            //if there is no text in tvOutput
            Toast.makeText(this, "there is no text detected", Toast.LENGTH_SHORT).show()
        } else {
            //if there is text in tvOutput
            mTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}
