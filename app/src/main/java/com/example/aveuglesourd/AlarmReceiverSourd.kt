package com.example.aveuglesourd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiverSourd : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var getNom: String = intent!!.getStringExtra("Nom")
        var i = Intent(context, AlarmOnActivitySourd::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.putExtra("Nom", getNom)
        context?.startActivity(i)
    }
}