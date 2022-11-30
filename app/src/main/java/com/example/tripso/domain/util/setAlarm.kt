package com.example.tripso.domain.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent


fun Context.setAlarm(time:Long,title:String,description:String){
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, TripAlarm::class.java)
    intent.apply {
        putExtra("title",title)
        putExtra("description",description)
    }
    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT)
    alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
}