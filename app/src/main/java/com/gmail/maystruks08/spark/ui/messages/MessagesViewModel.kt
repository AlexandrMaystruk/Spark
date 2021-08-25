package com.gmail.maystruks08.spark.ui.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.maystruks08.domain.entity.Cursor
import com.gmail.maystruks08.domain.entity.exceptions.MessageNotFoundException
import com.gmail.maystruks08.domain.use_cases.DeleteMessageUseCase
import com.gmail.maystruks08.domain.use_cases.ProvideInboxItemsUseCase
import com.gmail.maystruks08.domain.use_cases.ProvidePagingInboxItemsUseCase
import com.gmail.maystruks08.domain.use_cases.SwitchReadReadMessageStateUseCase
import com.gmail.maystruks08.spark.services.FirebaseServiceBus
import com.gmail.maystruks08.spark.ui.spark_adapter.base.Item
import com.gmail.maystruks08.spark.ui.utils.view_models.BottomView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesViewModel @Inject constructor(
    private val inboxViewMapper: InboxViewMapper,
    private val provideInboxItemsUseCase: ProvideInboxItemsUseCase,
    private val providePagingInboxItemsUseCase: ProvidePagingInboxItemsUseCase,
    private val changeMessageUseCase: SwitchReadReadMessageStateUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val firebaseServiceBus: FirebaseServiceBus
) : ViewModel() {

    private var cursor: Cursor? = null

    private val _messagesFlow = MutableStateFlow<MessageState>(MessageState.Loading)
    val messages get(): StateFlow<MessageState> = _messagesFlow

    private val _navigationFlow = Channel<NavigationState>(Channel.BUFFERED)
    val navigation = _navigationFlow.receiveAsFlow()

    init {
        viewModelScope.launch {
            firebaseServiceBus
                .subscribeToEvent()
                .receiveAsFlow()
                .collect { updateMessageListState() }
        }
    }

    fun loadMoreData() {
        if (cursor?.hasNext == false) return
        viewModelScope.launch {
            try {
                providePagingInboxItemsUseCase
                    .invoke(cursor)
                    .map {
                        cursor = it.cursor
                        inboxViewMapper.toInboxView(it.data)
                    }
                    .collect {
                        _messagesFlow.value = MessageState.ShowInboxList(
                            (_messagesFlow.value as? MessageState.ShowInboxList)?.let { previousState ->
                                previousState.data.toMutableList().apply { addAll(it) }
                            } ?: kotlin.run { it }
                        )
                    }
            } catch (t: Throwable) {
                handleInboxLoadingError(t)
            }
        }
    }

    fun onMessageItemClicked(item: MessageView) {
        viewModelScope.launch {
            _navigationFlow.send(NavigationState.OpenDetailScreen(item))
        }
    }

    fun onReadMessageItemClicked(item: MessageView) {
        viewModelScope.launch {
            try {
                changeMessageUseCase.invoke(item.id)
                updateMessageListState()
            } catch (t: Throwable) {
                handleError(t)
            }
        }
    }

    fun onUnreadMessageItemClicked(item: MessageView) {
        viewModelScope.launch {
            try {
                deleteMessageUseCase.invoke(item.id)
                updateMessageListState()
            } catch (t: Throwable) {
                handleError(t)
            }
        }
    }

    fun onDeleteMessageClicked(item: MessageView) {
        viewModelScope.launch {
            try {
                deleteMessageUseCase.invoke(item.id)
                updateMessageListState()
            } catch (t: Throwable) {
                handleError(t)
            }
        }
    }

    fun onShowAllMessageFromGroupClicked(item: BottomView) {
        viewModelScope.launch {
            provideInboxItemsUseCase
                .invoke(item.group)
                .map { messages -> messages.map { inboxViewMapper.toView(it) } }
                .collect {
                    _messagesFlow.value = MessageState.ShowInboxList(it)
                }
        }
    }

    fun onMessageSwipedLeft(swipedMessage: Item) {
        //TODO
    }

    fun onMessageSwipedRight(swipedMessage: Item) {
        //TODO
    }

    private fun updateMessageListState() {
//       loadMoreData()
        // provideMessageList()
    }

    private fun handleInboxLoadingError(throwable: Throwable) {
        Log.e("VIEW_MODEL", throwable.stackTraceToString())
        _messagesFlow.value = when (throwable) {
            is MessageNotFoundException -> MessageState.Error("Message not found")
            else -> MessageState.Error("Internal error")
        }
    }

    private fun handleError(throwable: Throwable) {
        Log.e("VIEW_MODEL", throwable.stackTraceToString())
        _messagesFlow.value = when (throwable) {
            is MessageNotFoundException -> MessageState.Error("Message not found")
            else -> MessageState.Error("Internal error")
        }
    }
}