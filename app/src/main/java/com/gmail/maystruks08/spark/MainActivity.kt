package com.gmail.maystruks08.spark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.gmail.maystruks08.spark.databinding.ActivityMainBinding
import com.gmail.maystruks08.spark.ui.messages.MessagesFragment

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        App.appComponent.inject(this)
        showMessageFragment()
    }

    private fun showMessageFragment() {
        supportFragmentManager.commit { replace(R.id.container, MessagesFragment.getInstance()) }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}