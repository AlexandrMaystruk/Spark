package com.gmail.maystruks08.spark.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.use_cases.NewMessageReceivedUseCase
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
    lateinit var newMessageReceivedUseCase: NewMessageReceivedUseCase

    @Inject
    lateinit var notification: NotificationController

    override fun onCreate() {
        super.onCreate()
        App.appComponent.inject(this)
        isServiceRunning = true
        serviceScope.launch(Dispatchers.IO) {
            delay(10 * 1000L)
            while (isServiceRunning) {
                val msg = generateFakeMessage()
                try {
                    notification.showNotification(
                        msg.date.time.toInt(),
                        "New message received",
                        msg.contentPreview,
                        msg.content
                    )
                    newMessageReceivedUseCase.invoke(msg)
                    receiveMessageBus.sendEvent(msg)
                } catch (e: Exception) {

                }
                delay(1 * 60 * 1000L)
            }
        }
    }

    private fun generateFakeMessage(): Message {
        return Message(
            id = UUID.randomUUID().toString(),
            date = Date(),
            from = "Andrey",
            subject = "Alex",
            contentPreview = "You could try to better optimize the user interface",
            group = "Others",
            content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
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
    }
}