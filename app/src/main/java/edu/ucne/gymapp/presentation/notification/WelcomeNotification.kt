package edu.ucne.gymapp.presentation.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@SuppressLint("MissingPermission")
fun showWelcomeNotification(context: Context) {
    createNotificationChannel(context)

    val builder = NotificationCompat.Builder(context, "GYM_CHANNEL")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("¡Bienvenido a Gym App!")
        .setContentText("¡Gracias por unirte a la familia de gymbros!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        notify(1, builder.build())
    }
}

private fun createNotificationChannel(context: Context) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Gym App Notifications"
        val descriptionText = "Notificaciones de la app de gimnasio"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("GYM_CHANNEL", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}