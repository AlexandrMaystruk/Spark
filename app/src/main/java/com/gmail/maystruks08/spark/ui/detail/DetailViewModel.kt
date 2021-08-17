package com.gmail.maystruks08.spark.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.maystruks08.domain.use_cases.ProvideMessageUseCase
import com.gmail.maystruks08.spark.ui.utils.toDetailView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageDetailView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val provideMessageUseCase: ProvideMessageUseCase
) : ViewModel() {

    val selectedMessage get() = _messagesFlow
    private val _messagesFlow = MutableStateFlow<MessageDetailView?>(null)

    fun requireMessage(messageId: String) {
        viewModelScope.launch {
            val message = provideMessageUseCase.invoke(messageId)
            val messageDetail = message.toDetailView()
            _messagesFlow.value = messageDetail
        }
    }

}