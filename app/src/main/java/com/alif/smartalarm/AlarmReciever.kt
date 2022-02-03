package com.alif.smartalarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*

class AlarmReciever : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        val type = intent?.getIntExtra("type", -1)
        val message = intent?.getStringExtra("message")
        val title = "One Time Alarm"

        val notificationId = 101

        if (message != null) showAlarmNotification(context, title, message, notificationId)
    }

    fun setOneTimeAlarm(context: Context, type: Int, date: String, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReciever::class.java)
        intent.putExtra("sesuatu", message)
        intent.putExtra("type", type)
        Log.e("ErrorSetOneTimeAlarm", "setOneTimeAlarm: $date $time")

        //  date received -> 2-2-2022
        //  deleting the "-" symbol -> 2 2 2022
        val dateArray = date.split("-").toTypedArray()

        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        //  date
        calendar.set(Calendar.DAY_OF_MONTH, converterData(dateArray)[0])
        calendar.set(Calendar.MONTH, converterData(dateArray)[1])
        calendar.set(Calendar.YEAR, converterData(dateArray)[2])
        //  time
        calendar.set(Calendar.HOUR, converterData(timeArray)[0])
        calendar.set(Calendar.MINUTE, converterData(timeArray)[1])
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Success Set One Time Alarm", Toast.LENGTH_SHORT).show()
    }

    fun converterData(array: Array<String>) : List<Int> {
        return array.map {
            it.toInt()
        }
    }

    private fun showAlarmNotification(
        context: Context?,
        title: String,
        message: String,
        notificationId: Int
    ) {
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_one_time)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(notificationId, notification)
    }
}