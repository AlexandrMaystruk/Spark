package com.gmail.maystruks08.spark.ui.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.maystruks08.domain.entity.Cursor
import com.gmail.maystruks08.domain.entity.exceptions.MessageNotFoundException
import com.gmail.maystruks08.domain.use_cases.*
import com.gmail.maystruks08.spark.services.FirebaseServiceBus
import com.gmail.maystruks08.spark.ui.utils.view_models.BottomView
import com.gmail.maystruks08.spark.ui.utils.view_models.InboxView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesViewModel @Inject constructor(
    private val inboxViewMapper: InboxViewMapper,
    private val provideInboxItemsUseCase: ProvideInboxItemsUseCase,
    private val provideGroupedInboxItemsUseCase: ProvideGroupedInboxItemsUseCase,
    private val providePagedInboxItemsUseCase: ProvidePagedInboxItemsUseCase,
    private val changeMessageUseCase: SwitchReadReadMessageStateUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val firebaseServiceBus: FirebaseServiceBus
) : ViewModel() {

    private var cursor: Cursor? = null

    private val _messagesFlow = MutableStateFlow<MessageState>(MessageState.Loading)
    val messages get(): StateFlow<MessageState> = _messagesFlow

    private val _navigationFlow = Channel<NavigationState>(Channel.BUFFERED)
    val navigation = _navigationFlow.receiveAsFlow()

    private val _modeFlow = MutableStateFlow<InboxMode>(InboxMode.Simple)

    private val _toolbarModeFlow = MutableStateFlow<ToolbarMode>(ToolbarMode.Standard)
    val fragmentToolbar get(): StateFlow<ToolbarMode> = _toolbarModeFlow

    init {
        viewModelScope.launch {
            firebaseServiceBus
                .subscribeToEvent()
                .receiveAsFlow()
                .collect { reloadMessages() }
        }

        viewModelScope.launch {
            _modeFlow
                .onEach {
                    cursor = null
                    _messagesFlow.value = MessageState.Loading
                    if (it is InboxMode.Group) _toolbarModeFlow.value = ToolbarMode.WithBackButton
                }
                .collect {
                    when (it) {
                        is InboxMode.Group -> {
                            loadMessagesGroup(it.name)
                        }
                        InboxMode.Simple -> loadMessages()
                        InboxMode.Smart -> loadSmartMessages()
                    }
                }
        }
    }

    fun loadMoreData() {
        if (cursor?.hasNext == false) return
        when (val value = _modeFlow.value) {
            is InboxMode.Group -> loadMessagesGroup(value.name)
            InboxMode.Simple -> loadMessages()
            InboxMode.Smart -> loadSmartMessages()
        }
    }

    fun changeInboxMode(isSwitchEnabled: Boolean) {
        _modeFlow.value = if (isSwitchEnabled) InboxMode.Smart else InboxMode.Simple
    }

    fun onBackPressed() {
        when (_toolbarModeFlow.value) {
            ToolbarMode.Standard -> {
                viewModelScope.launch { _navigationFlow.send(NavigationState.Exit) }
            }
            ToolbarMode.WithBackButton -> {
                _toolbarModeFlow.value = ToolbarMode.Standard
                _modeFlow.value = InboxMode.Simple
            }
        }
    }

    fun onMessageItemClicked(item: MessageView) {
        viewModelScope.launch {
            _navigationFlow.send(NavigationState.OpenDetailScreen(item))
        }
    }

    fun onReadMessageItemClicked(item: MessageView) {
        changeMessageReadState(item, true)
    }

    fun onUnreadMessageItemClicked(item: MessageView) {
        changeMessageReadState(item, false)
    }

    fun onDeleteMessageClicked(item: MessageView) {
        viewModelScope.launch {
            val currentState = getCurrentStateView()
            val index = currentState.indexOfFirst { (it as? MessageView)?.id == item.id }
            val result = currentState.apply {
                if (index == -1) return@apply
                removeAt(index)
            }
            try {
                _messagesFlow.value = MessageState.ShowInboxList(result.toList())
                deleteMessageUseCase.invoke(item.id)
            } catch (t: Throwable) {
                _messagesFlow.value = MessageState.ShowInboxList(result.apply { add(index, item) }.toList())
                handleError(t)
            }
        }
    }

    fun onShowAllMessageFromGroupClicked(item: BottomView) {
        _modeFlow.value = InboxMode.Group(item.group)
    }

    fun onMessageSwipedLeft(swipedMessage: MessageView) {
        onDeleteMessageClicked(swipedMessage)
    }

    fun onMessageSwipedRight(swipedMessage: MessageView) {
        if (swipedMessage.isRead) {
            onUnreadMessageItemClicked(swipedMessage)
            return
        }
        onReadMessageItemClicked(swipedMessage)
    }

    private fun reloadMessages() {
        cursor = null
        _messagesFlow.value = MessageState.Loading
        loadMoreData()
    }

    private fun loadSmartMessages() {
        viewModelScope.launch {
            try {
                provideGroupedInboxItemsUseCase
                    .invoke(cursor)
                    .map {
                        cursor = it.cursor
                        inboxViewMapper.toInboxView(it.data)
                    }
                    .collect {
                        _messagesFlow.value = MessageState.ShowInboxList(it)
                    }
            } catch (t: Throwable) {
                handleInboxLoadingError(t)
            }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            try {
                providePagedInboxItemsUseCase
                    .invoke(cursor)
                    .map {
                        cursor = it.cursor
                        inboxViewMapper.toViews(it.data)
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

    private fun loadMessagesGroup(group: String) {
        viewModelScope.launch {
            try {
                provideInboxItemsUseCase
                    .invoke(group, cursor)
                    .map {
                        cursor = it.cursor
                        inboxViewMapper.toViews(it.data)
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

    private fun changeMessageReadState(item: MessageView, isRead: Boolean){
        viewModelScope.launch {
            val currentState = getCurrentStateView()
            val index = currentState.indexOfFirst { (it as? MessageView)?.id == item.id }
            val oldItem = currentState[index] as? MessageView
            try {
                val result = currentState.apply {
                    if (index == -1 || oldItem == null) return@apply
                    this[index] = oldItem.copy(isRead = isRead)
                }
                _messagesFlow.value = MessageState.ShowInboxList(result.toList())
                changeMessageUseCase.invoke(item.id)
            } catch (t: Throwable) {
                handleError(t)
                //restore state
                val result = currentState.apply {
                    if (index == -1 || oldItem == null) return@apply
                    this[index] = oldItem
                }
                _messagesFlow.value = MessageState.ShowInboxList(result.toList())
            }
        }
    }

    private fun getCurrentStateView(): MutableList<InboxView> {
        return (_messagesFlow.value as? MessageState.ShowInboxList)?.data.orEmpty().toMutableList()
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