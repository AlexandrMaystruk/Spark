package com.gmail.maystruks08.spark.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.maystruks08.domain.entity.exceptions.MessageNotFoundException
import com.gmail.maystruks08.domain.use_cases.ProvideInboxItemsUseCase
import com.gmail.maystruks08.spark.ui.spark_adapter.base.Item
import com.gmail.maystruks08.spark.ui.utils.view_models.BottomView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesViewModel @Inject constructor(
    private val inboxViewMapper: InboxViewMapper,
    private val provideInbox: ProvideInboxItemsUseCase
) : ViewModel() {

    val messages get(): StateFlow<MessageState> = _messagesFlow
    private val _messagesFlow = MutableStateFlow<MessageState>(MessageState.Loading)

    fun provideMessageList(){
        viewModelScope.launch {
            try {
                provideInbox
                    .invoke()
                    .map { inboxViewMapper.toInboxView(it) }
                    .collect {
                        _messagesFlow.value = MessageState.ShowInboxList(it)
                    }
            } catch (t: Throwable) {
                handleInboxLoadingError(t)
            }
        }
    }

    fun onMessageItemClicked(item: MessageView) {

    }

    fun onMessageItemLongClicked(item: MessageView) {

    }

    fun onShowAllMessageFromGroupClicked(item: BottomView) {

    }

    fun onMessageSwipedLeft(swipedMessage: Item) {

    }

    fun onMessageSwipedRight(swipedMessage: Item) {

    }

    private fun handleInboxLoadingError(throwable: Throwable) {
        _messagesFlow.value = when (throwable) {
            is MessageNotFoundException -> MessageState.Error("Message not found")
            else -> MessageState.Error("Internal error")
        }
    }

}