package com.gmail.maystruks08.spark

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gmail.maystruks08.spark.databinding.ActivityMainBinding
import com.gmail.maystruks08.spark.services.FCMNotificationServiceMock
import com.gmail.maystruks08.spark.services.NotificationController
import com.gmail.maystruks08.spark.ui.utils.toolbar.FragmentToolbar
import com.gmail.maystruks08.spark.ui.utils.toolbar.ToolbarManager
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationController: NotificationController

    private var toolbarManager: ToolbarManager? = null
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        App.appComponent.inject(this)
        toolbarManager = ToolbarManager(binding!!.toolbar)
        runMockFirebaseMessagingService()
        handleIntent()
    }

    fun configToolbar(fragmentToolbar: FragmentToolbar) {
        toolbarManager?.prepareToolbar(fragmentToolbar)
    }

    private fun handleIntent() {
        intent?.extras?.let {
            val isStartedFromNotification = it.getBoolean(NotificationController.IS_ACTIVITY_STARTED_FROM_NOTIFICATION)
            if (isStartedFromNotification) notificationController.removeAllNotifications()
        }
    }

    private fun runMockFirebaseMessagingService() {
        if (!FCMNotificationServiceMock.isServiceRunning) {
            startService(Intent(this, FCMNotificationServiceMock::class.java))
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}

