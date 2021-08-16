package com.gmail.maystruks08.spark.ui.messages

import androidx.lifecycle.ViewModel
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MessagesViewModel @Inject constructor() : ViewModel() {

    val messages get() = _messagesFlow
    private val _messagesFlow = MutableStateFlow<MutableList<MessageView>>(mutableListOf())

}