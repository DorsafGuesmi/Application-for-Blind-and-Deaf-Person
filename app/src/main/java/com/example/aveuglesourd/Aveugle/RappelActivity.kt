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

    lateinit var NomM: EditText
    lateinit var am: AlarmManager
    lateinit var tp: TimePicker
    lateinit var update_text: TextView
    lateinit var con: Context
    lateinit var btnStart: Button
    lateinit var btnStop: Button
    var hour: Int = 0
    var min: Int = 0
    lateinit var pi: PendingIntent

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    val channelId = "com.example.pfeapp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rappel)

        this.con = this
        am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        tp = findViewById<TimePicker>(R.id.timePicker)
        update_text = findViewById<TextView>(R.id.timeView)
        btnStart = findViewById<Button>(R.id.start_alarm)
        btnStop = findViewById<Button>(R.id.stop_alarm)
        var calendar: Calendar = Calendar.getInstance()
        var myintent: Intent = Intent(this, AlarmReceiver::class.java)

        btnStart.setOnClickListener {
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
            myintent.putExtra("extra","on")
            pi = PendingIntent.getBroadcast(this@RappelActivity, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            }
        }
        btnStop.setOnClickListener{
            set_alarm_text("Alarm off")
            pi = PendingIntent.getBroadcast(this, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT)
            am.cancel(pi)
            myintent.putExtra("extra","off")
            sendBroadcast(myintent)
        }
    }

    private fun set_alarm_text(s: String) {
        update_text.setText(s)
    }

}
