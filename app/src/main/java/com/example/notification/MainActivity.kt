package com.example.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notification.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.action == "Okay") {
            Toast.makeText(this, "okay clicked", Toast.LENGTH_LONG).show()
        }
        createNotificationChannel()
        createNotificationChannel1()

        binding.submitButton.setOnClickListener { scheduleNotification() }
        binding.actionButton.setOnClickListener { showNotificationWithAction() }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification() {
        val intent = Intent(this, Notification::class.java)

        val title = binding.titleET.text.toString()
        val message = binding.messageET.text.toString()

        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            this, notificationID, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, time, pendingIntent
        )

        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {

        val date = Date(time)
        val dateFormate = android.text.format.DateFormat.getLongDateFormat(this)
        val timeFormate = android.text.format.DateFormat.getTimeFormat(this)

        AlertDialog.Builder(this)
            .setTitle("notification schedule")
            .setMessage(
                "title = " + title + "\n message=" + message
                        + "\nAt: " + dateFormate.format(date) + " " + timeFormate.format(date)
            ).setPositiveButton("Okay") { _, _ -> }
            .create().show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hours = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hours, minute)
        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "notif channel"
        val desc = "A Description of the channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


    }

    private fun createNotificationChannel1() {
        val name = "notif action channel"
        val desc = "A Description of the channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID2, name, importance)
        channel.description = desc

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotificationWithAction() {

        val intent2 = Intent(this@MainActivity, MainActivity::class.java).apply {
            action = "Not Okay"
        }
        val intent1 = Intent(this@MainActivity, MainActivity::class.java).apply {
            action = "Okay"

        }

        val pendingIntent2 =
            PendingIntent.getActivity(
                this,
                0,
                intent2,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val pendingIntent1 = PendingIntent.getBroadcast(
            this, 0, intent1,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, channelID2).apply {
            setSmallIcon(R.drawable.ic_baseline_notifications_24)
            setContentTitle("action test")
            setContentText("action tested")
            addAction(R.drawable.ic_baseline_cancel_24, "Not Okay", pendingIntent2)
            addAction(R.drawable.ic_baseline_cancel_24, "Okay", pendingIntent1)
            priority = NotificationCompat.PRIORITY_HIGH
        }

        with(NotificationManagerCompat.from(this@MainActivity)) {
            notify(notificationID2, builder.build())

        }

    }
}
