package com.gmail.maystruks08.spark.ui.detail

import androidx.lifecycle.ViewModel
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel() {

    val messages get() = _messagesFlow
    private val _messagesFlow = MutableStateFlow<MessageView?>(null)

}