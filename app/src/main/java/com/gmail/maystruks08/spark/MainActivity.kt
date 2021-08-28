package com.gmail.maystruks08.spark

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.gmail.maystruks08.spark.databinding.ActivityMainBinding
import com.gmail.maystruks08.spark.services.FCMNotificationServiceMock
import com.gmail.maystruks08.spark.services.NotificationController
import com.gmail.maystruks08.spark.ui.messages.MessagesFragment
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
        handleStartIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleStartIntent(intent)
    }

    fun configToolbar(fragmentToolbar: FragmentToolbar) {
        toolbarManager?.prepareToolbar(fragmentToolbar)
    }

    private fun handleStartIntent(intent: Intent?) {
        intent?.let {
            val isStartedFromNotification = it.getBooleanExtra(NotificationController.IS_ACTIVITY_STARTED_FROM_NOTIFICATION, false)
            val messageId = it.getStringExtra(NotificationController.NOTIFICATION_MESSAGE_ID).orEmpty()
            if (isStartedFromNotification) {
                val navHostFragment = supportFragmentManager.fragments.firstOrNull() as NavHostFragment
                val messagesFragment = navHostFragment.childFragmentManager.fragments.firstOrNull() as MessagesFragment
                messagesFragment.viewModel.onNewMessageNotificationClicked(messageId)
                notificationController.removeAllNotifications()
            }
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

