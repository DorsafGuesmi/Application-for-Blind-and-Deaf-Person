package com.example.aveuglesourd.Aveugle

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.aveuglesourd.R
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector
import kotlinx.android.synthetic.main.activity_document.*
import java.io.File
import java.util.*

class DocumentActivity : AppCompatActivity() {

    lateinit var btCam: Button
    lateinit var imgView: ImageView
    lateinit var fileFinalPath: File
    private var uriFinale: Uri? = null
    val PERMISSION_REQ_CODE: Int = 100
    lateinit var path: String
    lateinit var mTTS: TextToSpeech
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)
        init()
    }

    private fun init() {
        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.UK
            }
        })
        btCam = findViewById(R.id.btCam)
        imgView = findViewById(R.id.image_View)
        btCam.setOnClickListener {
            i++

            val handler = Handler()
            handler.postDelayed({
                if (i == 1) {
                    mTTS.speak("double click on this button to open camera", TextToSpeech.QUEUE_FLUSH, null)
                } else if (i == 2) {
                    onClick()
                }
                i = 0
            }, 500)
        }
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
                onDeviceRecognizeText(bitmap)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun onDeviceRecognizeText(bitmap: Bitmap) {
        var text: String = ""
        val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)
        val detector: FirebaseVisionTextDetector = FirebaseVision.getInstance().visionTextDetector
        var result : Task<FirebaseVisionText> = detector.detectInImage(image)
            .addOnSuccessListener { p0 ->
                for (block: FirebaseVisionText.Block in p0!!.blocks){
                    var boundingBox: Rect = block.boundingBox!!
                    var cornerPoints : Array<Point> = block.cornerPoints!!
                    //text+=block.text
                    for(line: FirebaseVisionText.Line in block.lines){
                        text+= line.text + "\n"
                    }
                }
                tvOutput.setText(text)
                textToSpeech(tvOutput)
            }
            .addOnFailureListener {
                    p0 -> Toast.makeText(this@DocumentActivity,p0.message, Toast.LENGTH_LONG).show()
            }
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

    companion object {
        fun getPath(uri: Uri, activity: DocumentActivity): String? {
            if (uri == null)
                return null
            var projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = activity.contentResolver.query(uri, projection, null, null, null)
            var column_index: Int
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                return cursor.getString(column_index)
            }
            return uri.path
        }

        fun getPath1(uri: Uri, activity: DocumentActivity): String? {
            var projection: Array<String> = arrayOf(MediaStore.MediaColumns.DATA)
            var cursor = activity.contentResolver.query(uri, projection, null, null, null)
            var column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        }
    }
}
