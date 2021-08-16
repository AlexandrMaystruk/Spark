package com.gmail.maystruks08.spark.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.maystruks08.domain.use_cases.ProvideInboxItemsUseCase
import com.gmail.maystruks08.spark.ui.utils.toView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesViewModel @Inject constructor(
    private val provideInbox: ProvideInboxItemsUseCase
) : ViewModel() {

    val messages get(): StateFlow<List<MessageView>> = _messagesFlow
    private val _messagesFlow = MutableStateFlow<List<MessageView>>(emptyList())

    init {
        viewModelScope.launch() {
            provideInbox
                .invoke()
                .catch { throwable -> handleError(throwable) }
                .map { messages -> messages.map { it.toView() } }
                .collect {
                    _messagesFlow.value = it
                }
        }
    }

    fun onMessageItemClicked(item: MessageView) {

    }

    fun onMessageItemLongClicked(item: MessageView) {

    }

    private fun handleError(throwable: Throwable) {

    }

}