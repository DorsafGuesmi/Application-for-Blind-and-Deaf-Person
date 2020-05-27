package com.example.aveuglesourd.Aveugle

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import com.example.aveuglesourd.R
import java.util.*

class RappelActivity : AppCompatActivity() {

    lateinit var am: AlarmManager
    lateinit var tp: TimePicker
    lateinit var update_text: TextView
    lateinit var con: Context
    lateinit var btnStart: Button
    lateinit var btnStop: Button
    lateinit var etMedicament: TextView
    var hour: Int = 0
    var min: Int = 0
    lateinit var pi: PendingIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rappel)


        this.con = this
        am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        tp = findViewById<TimePicker>(R.id.timePicker)
        update_text = findViewById<TextView>(R.id.timeView)
        etMedicament = findViewById<TextView>(R.id.et1)
        btnStart = findViewById<Button>(R.id.start_alarm)
        btnStop = findViewById<Button>(R.id.stop_alarm)

        btnStart.setOnClickListener {
            val NomMedicament = etMedicament.text.toString()
            var calendar: Calendar = Calendar.getInstance()
            var myintent: Intent = Intent(applicationContext, AlarmReceiver::class.java)

            myintent.putExtra("Nom", NomMedicament)
            pi = PendingIntent.getBroadcast(applicationContext, 0, myintent, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                calendar.set(Calendar.HOUR_OF_DAY,tp.hour)
                calendar.set(Calendar.MINUTE,tp.minute)
                calendar.set(Calendar.SECOND,0)
                calendar.set(Calendar.MILLISECOND,0)
                hour = tp.hour
                min = tp.minute
            } else {
                calendar.set(Calendar.HOUR_OF_DAY,tp.currentHour)
                calendar.set(Calendar.MINUTE,tp.currentMinute)
                calendar.set(Calendar.SECOND,0)
                calendar.set(Calendar.MILLISECOND,0)
                hour = tp.currentHour
                min = tp.currentMinute
            }
            var hr_str: String = hour.toString()
            var min_str: String = min.toString()
            if (hour > 12){
                hr_str = (hour - 12).toString()
            }
            if (min < 10){
                min_str = "0$min"
            }
            set_alarm_text("Alarme réglée sur: $hr_str : $min_str")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            }

        }
        btnStop.setOnClickListener{
            set_alarm_text("Alarm off")
            am.cancel(pi)
        }
    }

    private fun set_alarm_text(s: String) {
        update_text.setText(s)
    }

}
