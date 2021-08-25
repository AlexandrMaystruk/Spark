package com.gmail.maystruks08.spark.ui.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

fun <T> MutableList<T>.updateElement(element: T, predicate: (T) -> Boolean) {
    val iterator = iterator()
    var index = 0
    while (iterator.hasNext()) {
        val item = iterator.next()
        if (predicate(item)) {
            iterator.remove()
            add(index, element)
            return
        }
        index++
    }
}

class FlowObserver<T> (
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>,
    private val collector: suspend (T) -> Unit
) {
    private var job: Job? = null

    init {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver {
                source: LifecycleOwner, event: Lifecycle.Event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    job = source.lifecycleScope.launch {
                        flow.collect { collector(it) }
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    job?.cancel()
                    job = null
                }
                else -> Unit
            }
        })
    }
}

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})
