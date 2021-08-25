package com.gmail.maystruks08.spark

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gmail.maystruks08.spark.services.FCMNotificationServiceMock
import com.gmail.maystruks08.spark.databinding.ActivityMainBinding
import com.gmail.maystruks08.spark.ui.utils.toolbar.FragmentToolbar
import com.gmail.maystruks08.spark.ui.utils.toolbar.ToolbarManager

class MainActivity : AppCompatActivity() {

    var toolbarManager: ToolbarManager? = null

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        App.appComponent.inject(this)
        toolbarManager = ToolbarManager(binding!!.toolbar)
        runMockFirebaseMessagingService()
    }

    fun configToolbar(fragmentToolbar: FragmentToolbar) {
        toolbarManager?.prepareToolbar(fragmentToolbar)
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

