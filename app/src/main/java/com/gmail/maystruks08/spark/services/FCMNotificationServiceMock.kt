package com.gmail.maystruks08.spark.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.spark.App
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class FCMNotificationServiceMock : Service() {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    @Inject
    lateinit var receiveMessageBus: FirebaseServiceBus

    @Inject
    lateinit var notification: NotificationController

    override fun onCreate() {
        super.onCreate()
        App.appComponent.inject(this)
        isServiceRunning = true
        serviceScope.launch(Dispatchers.IO) {
            while (isServiceRunning) {
                delay(15 * 1000L)
                val msg = generateFakeMessage()
                try {
                    notification.showNotification(
                        msg.date.time.toInt(),
                        "New message received",
                        msg.contentPreview,
                        msg.content
                    )
                    receiveMessageBus.sendEvent(msg)
                } catch (e: Exception) { }
            }
        }
    }

    private fun generateFakeMessage(): Message {
        return Message(
            id = Random().nextInt(100).toString(),
            date = Date(),
            from = "Random message ${Random().nextInt(100)}",
            header = "New message simulation",
            subject = UUID.randomUUID().toString(),
            contentPreview = source[Random().nextInt(2)].take(100),
            group = "Push mock",
            content = source[Random().nextInt(2)],
            isRead = false,
            isDeleted = false
        )
    }

    override fun onDestroy() {
        serviceJob.cancel()
        isServiceRunning = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        var isServiceRunning = false
        private val source = listOf(
            "Sorry, I haven't had time to implement the JavaMail library. I was in vacation",
            "I hope my test app is not bad=)",
            "I think there exist a big bug bucket"
        )
    }
}