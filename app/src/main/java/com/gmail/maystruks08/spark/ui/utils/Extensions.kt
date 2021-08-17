package com.gmail.maystruks08.spark.ui.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get(T::class.java)
}

inline fun <reified T : ViewModel> AppCompatActivity.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get(T::class.java)
}


const val DATE_FORMAT = "dd.MM.yyyy"
const val TIME_FORMAT = "HH:mm:ss"
const val DATE_TIME_FORMAT = "$DATE_FORMAT $TIME_FORMAT"
fun Date.toPrintFormat(): String {
    val sdf = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    return sdf.format(this)
}