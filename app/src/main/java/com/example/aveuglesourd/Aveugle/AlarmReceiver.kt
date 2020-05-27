package com.example.aveuglesourd.Aveugle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var getNom: String = intent!!.getStringExtra("Nom")
        var i = Intent(context, AlarmOnActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.putExtra("Nom", getNom)
        context?.startActivity(i)
    }
}