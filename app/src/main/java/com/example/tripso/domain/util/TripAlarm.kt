package com.example.tripso.domain.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.util.Log
import java.util.*


class TripAlarm : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        try {
            showNotification(context, noteId = Random().nextInt(100),
                title = intent.getStringExtra("title").toString(),
                desc = intent.getStringExtra("description").toString())
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(2000)
        } catch (ex: Exception) {
            Log.d("Receive Exception", "onReceive: ${ex.printStackTrace()}")
        }
    }

}