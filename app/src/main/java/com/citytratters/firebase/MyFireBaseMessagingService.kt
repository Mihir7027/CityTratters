package com.citytratters.firebase

import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.citytratters.R
import com.citytratters.ui.activity.SignInActivity
import org.json.JSONException
import java.util.HashMap


class MyFireBaseMessagingService : FirebaseMessagingService() {

    private val TAG = MyFireBaseMessagingService::class.java.simpleName

    private val NOTIFICATION_ID = 100

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)

        // Check if message contains a ic_small_notification payload.
        if (remoteMessage.notification != null) {
            Log.e(
                TAG,
                "Notification Body: " + remoteMessage.notification!!.body
            )
            //remoteMessage.notification!!.body?.let { handleNotification(it) }
        }

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Data Payload: " + remoteMessage.data.toString())
            try {
               /* val json = JSONObject(remoteMessage.data.toString())*/

                var mHeaderMap: MutableMap<String, String> = HashMap()

                mHeaderMap = remoteMessage.data
                val title =  mHeaderMap["Title"]
                val message =  mHeaderMap["Message"]
                val idNumber =  mHeaderMap["RefId"]
                Log.e("order id",idNumber.toString())
                handleDataMessage(message,title,idNumber)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }
    }
/*
    private fun handleNotification(message: String) {
        if (!isAppIsInBackground(applicationContext)) {
            // app is in foreground, broadcast the push message

                val pushNotification = Intent("pushNotification")
                pushNotification.putExtra("message", message)
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)


            // play ic_small_notification sound
            playNotificationSound()
        } else {
            // If the app is in background, firebase itself handles the ic_small_notification
            val title = "NeoShine"
            var resultIntent: Intent? = null
            resultIntent = Intent(this, MainActivity::class.java)
            showNotificationMessage(applicationContext, title, message, "", resultIntent)
        }
    }*/

    private fun handleDataMessage(message: String?,title:String?,idNumber:String?) {

        try {
            val appTitle = "NeoShine"

            if (!isAppIsInBackground(applicationContext)) {
                // app is in foreground, broadcast the push message

                val pushNotification = Intent("pushNotification")
                pushNotification.putExtra("message", message)
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

                // play ic_small_notification sound

            } else {
                // app is in background, show the ic_small_notification in ic_small_notification tray


                var resultIntent: Intent? = null
                resultIntent = Intent(this, SignInActivity::class.java)
                resultIntent!!.putExtra("idNumber",idNumber.toString())


                showNotificationMessage(applicationContext, title.toString(), message.toString(), "", resultIntent)

            }
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }

   /* private fun handleDataMessage(json: JSONObject) {
        Log.e(TAG, "push json: $json")
        try {
            val title = "NeoShine"
            val message = json.getString("push_from")
            if (!isAppIsInBackground(applicationContext)) {
                // app is in foreground, broadcast the push message

                    val pushNotification = Intent("pushNotificationFor911Detail")
                    pushNotification.putExtra("message", message)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)


                // play ic_small_notification sound

            } else {
                // app is in background, show the ic_small_notification in ic_small_notification tray
                var resultIntent: Intent? = null
                resultIntent = Intent(this, MainActivity::class.java)
                showNotificationMessage(applicationContext, title, message, "", resultIntent)

            }
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }*/

    /**
     * Showing ic_small_notification with text only
     */
    fun showNotificationMessage(
        context: Context,
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent
    ) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        showNotificationMessage(title, message, timeStamp, intent)
    }


    private fun showNotificationMessage(
        title: String?,
        message: String?,
        timeStamp: String?,
        intent: Intent
    ) {
        // Check for empty push message
        if (TextUtils.isEmpty(message)) return


        // ic_small_notification icon
        //val icon: Int = R.drawable.ic_small_notification
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val mBuilder = NotificationCompat.Builder(
            applicationContext
        )
     val alarmSound = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + applicationContext!!.packageName + "/" + R.raw.notification_sound
        )

        /*showSmallNotification(
            mBuilder,
            icon,
            title,
            message,
            timeStamp,
            resultPendingIntent,
             alarmSound
        )*/
    }

    private fun showSmallNotification(
        mBuilder: NotificationCompat.Builder,
        icon: Int,
        title: String?,
        message: String?,
        timeStamp: String?,
        resultPendingIntent: PendingIntent,
        alarmSound: Uri
    ) {
        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.addLine(message)
        val channelId = this.getString(R.string.default_notification_channel_id)
        var notificationManager =
            applicationContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo ic_small_notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("Bov kri ho", "yes")
            val name: CharSequence = this.getString(R.string.channel_name)
            val description = this.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other ic_small_notification behaviors after this
            notificationManager =
                applicationContext!!.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        val notification: Notification
       /* notification = mBuilder.setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setSound(alarmSound)
            .setStyle(inboxStyle)
            .setSmallIcon(R.drawable.ic_small_notification)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext!!.resources, icon))
            .setContentText(message)
            .setChannelId(channelId)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)*/
    }

    // Playing ic_small_notification sound
    private fun playNotificationSound() {
        try {
            val alarmSound = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + applicationContext!!.packageName + "/" + R.raw.notification_sound
            )
            val r = RingtoneManager.getRingtone(applicationContext, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses =
            am.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.packageName) {
                        isInBackground = false
                    }
                }
            }
        }
        return isInBackground
    }


}