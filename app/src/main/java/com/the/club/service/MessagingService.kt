package com.the.club.service

import android.annotation.SuppressLint
import android.app.Notification.DEFAULT_SOUND
import android.app.Notification.DEFAULT_VIBRATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import com.the.club.MainActivity
import com.the.club.R
import java.util.*


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    private var singleNotificationId = 10
    private var bundleNotificationId = 10

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["title"] ?: ""
        val body = remoteMessage.data["message"] ?: ""
        val extras = remoteMessage.data["params"] ?: ""
        showNotification(body, title, extras)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(body: String, title: String, extras: String) {
        val id = System.currentTimeMillis().toInt()
        val bm = BitmapFactory.decodeResource(application.resources, R.drawable.ic_logo)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val groupKey = "bundle_notification_$bundleNotificationId"
        var resultIntent = Intent(applicationContext, MainActivity::class.java)
        resultIntent.putExtra("payloadExtraData", extras)
        resultIntent.putExtra("notification_id", bundleNotificationId)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        var resultPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getActivity(this, bundleNotificationId, resultIntent, FLAG_IMMUTABLE)
        } else {
            getActivity(this, bundleNotificationId, resultIntent, FLAG_ONE_SHOT)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.notificationChannels.size < 2) {
                val groupChannel = NotificationChannel("bundle_channel_id", "bundle_channel_name", NotificationManager.IMPORTANCE_LOW)
                notificationManager.createNotificationChannel(groupChannel)
                val channel = NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }
        }

        val summaryNotificationBuilder = NotificationCompat.Builder(this, "bundle_channel_id")
            .setGroup(groupKey)
            .setGroupSummary(true)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_logo)
            .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE)
            .setLargeIcon(bm)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)

        if (singleNotificationId == bundleNotificationId)
            singleNotificationId = bundleNotificationId
        else
            ++singleNotificationId

        resultIntent = Intent(applicationContext, MainActivity::class.java)
        resultIntent.putExtra("payloadExtraData", extras)
        resultIntent.putExtra("notification_id", singleNotificationId)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        resultPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getActivity(this, Random().nextInt(), resultIntent, FLAG_MUTABLE)
        } else {
            getActivity(this, Random().nextInt(), resultIntent, FLAG_ONE_SHOT)
        }

        val notification = NotificationCompat.Builder(this, "channel_id")
            .setGroup(groupKey)
            .setContentTitle(title)
            .setContentText(body)
            //.setNumber(++singleNotificationId)
            .setSmallIcon(R.drawable.ic_logo)
            .setLargeIcon(bm)
            .setSound(defaultSoundUri)
            .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE)
            .setAutoCancel(true)
            .setGroupSummary(false)
            .setContentIntent(resultPendingIntent)
        notificationManager.notify(id, notification.build())
        notificationManager.notify(bundleNotificationId, summaryNotificationBuilder.build())
    }
}
