package com.example.tripso.domain.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.tripso.R


class TripAlarm : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        try {
            showNotification(context, noteId = 1,"Trip Reminder", desc = "")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(2000)
        } catch (ex: Exception) {
            Log.d("Receive Exception", "onReceive: ${ex.printStackTrace()}")
        }
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(context: Context,noteId:Int, title: String, desc: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "message_channel"
        val channelName = "message_name"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(desc)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)

        manager.notify(noteId, builder.build())
    }
}