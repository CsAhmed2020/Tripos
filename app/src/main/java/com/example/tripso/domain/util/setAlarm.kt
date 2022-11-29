package com.example.tripso.domain.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent


fun Context.setAlarm(time:Long){
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, TripAlarm::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent,0)
    alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
}