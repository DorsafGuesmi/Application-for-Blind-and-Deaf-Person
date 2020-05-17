package com.example.aveuglesourd.Aveugle

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.aveuglesourd.R
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import kotlinx.android.synthetic.main.activity_label.*
import java.io.File
import java.util.*

class LabelActivity : AppCompatActivity() {

    lateinit var btCam: Button
    lateinit var imgView: ImageView
    lateinit var fileFinalPath: File
    private var uriFinale: Uri? = null
    val PERMISSION_REQ_CODE: Int = 100
    lateinit var path: String
    lateinit var mTTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_label)
        init()
    }

    private fun init() {
        btCam = findViewById(R.id.btCamLabel)
        imgView = findViewById(R.id.image_View_Label)
        btCam.setOnClickListener {
            onClick()
        }
        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.UK
            }
        })
    }

    fun onClick() {
        //if system OS is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission was not enable
                val permission = arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                //show popup to request permission
                requestPermissions(permission, PERMISSION_REQ_CODE)
            } else {
                //permission already granted
                OpenCamera()
            }
        } else {
            //system OS is < Marshmallow
            OpenCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQ_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    OpenCamera()
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun OpenCamera() {
        val values = ContentValues()
        /*  values.put(MediaStore.Images.Media.TITLE,"New Picture")
          values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera")*/
        fileFinalPath = File(
            Environment.getExternalStorageDirectory(),
            "Image_" + System.currentTimeMillis() + ".jpg"
        )
        uriFinale =
            FileProvider.getUriForFile(applicationContext, packageName + ".provider", fileFinalPath)
        //Camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFinale)
        startActivityForResult(cameraIntent, PERMISSION_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                val pictureUri: Uri = Uri.fromFile(fileFinalPath)
                val file: File = File(pictureUri.path)
                path = file.absolutePath
                val bmOptions: BitmapFactory.Options = BitmapFactory.Options()
                val bitmap: Bitmap = BitmapFactory.decodeFile(path, bmOptions)
                imgView.setImageBitmap(bitmap)
                imageLabel(bitmap)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun imageLabel(bitmap: Bitmap){
        val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().visionLabelDetector
        detector.detectInImage(image)
            .addOnSuccessListener {labels ->
                processLabels(labels)
            }
            .addOnFailureListener {
                    labels -> Toast.makeText(this@LabelActivity,labels.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun processLabels(labels: List<FirebaseVisionLabel>) {
        val lbl = labels.firstOrNull()
        var msg = lbl?.label + "," + lbl?.confidence
        updateLabel(msg)
        for (label in labels){
            val text = label.label
            val entityId = label.entityId
            val confidence = label.confidence

            Log.d("ImageLabel", text + entityId + confidence)
        }
    }

    private fun updateLabel(message: String) {
        this.tvOutputLabel.text = message
    }

    private fun textToSpeech(tvOutput: TextView) {
        val toSpeak = tvOutput.text.toString()
        if (toSpeak == "") {
            //if there is no text in tvOutput
            Toast.makeText(this, "there is no text detected", Toast.LENGTH_SHORT).show()
        } else {
            //if there is text in tvOutput
            mTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}
