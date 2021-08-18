package com.gmail.maystruks08.spark.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.maystruks08.domain.entity.exceptions.MessageNotFoundException
import com.gmail.maystruks08.domain.use_cases.DeleteMessageUseCase
import com.gmail.maystruks08.domain.use_cases.ProvideMessageUseCase
import com.gmail.maystruks08.domain.use_cases.SwitchReadReadMessageStateUseCase
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.ui.messages.InboxViewMapper
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageDetailView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val inboxViewMapper: InboxViewMapper,
    private val provideMessageUseCase: ProvideMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val switchReadReadMessageStateUseCase: SwitchReadReadMessageStateUseCase

) : ViewModel() {

    val selectedMessage get() = _messageDetailFlow
    private val _messageDetailFlow = MutableStateFlow<DetailViewState>(DetailViewState.Loading)

    val buttonState get() = _buttonStateFlow
    private val _buttonStateFlow =
        MutableStateFlow<DetailViewButtonState>(DetailViewButtonState.Hide)

    fun requireMessage(messageId: String) {
        viewModelScope.launch {
            try {
                val message = provideMessageUseCase.invoke(messageId)
                val detailView = inboxViewMapper.toDetailView(message)
                _messageDetailFlow.value = DetailViewState.ShowMessage(detailView)
                handleButtonsState(message.isRead, message.isDeleted)
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    private fun handleButtonsState(isMessageRead: Boolean, isDeleted: Boolean) {
        if (isDeleted) {
            _buttonStateFlow.value = DetailViewButtonState.Hide
            return
        }
        val resId = if (isMessageRead) R.string.action_unread else R.string.action_read
        val title = inboxViewMapper.getString(resId)
        _buttonStateFlow.value = DetailViewButtonState.Show(title)
    }

    fun onChangeReadStatusClicked(messageId: String) {
        //works by a positive scenario
        getCurrentDetailView()?.let { handleButtonsState(!it.isRead, it.isDeleted) }
        viewModelScope.launch {
            try {
                val updatedMessage = switchReadReadMessageStateUseCase.invoke(messageId)
                getCurrentDetailView()?.isRead = updatedMessage.isRead
            } catch (t: Throwable) {
                //after error render previous state
                getCurrentDetailView()?.let { handleButtonsState(it.isRead, it.isDeleted) }
                onError(t)
            }
        }
    }

    fun onDeleteMessageClicked(messageId: String) {
        //works by a positive scenario
        getCurrentDetailView()?.let { handleButtonsState(it.isRead, true) }
        viewModelScope.launch {
            try {
                val updatedMessage = deleteMessageUseCase.invoke(messageId)
                getCurrentDetailView()?.isDeleted = updatedMessage.isDeleted
            } catch (t: Throwable) {
                //after error render previous state
                getCurrentDetailView()?.let { handleButtonsState(it.isRead, it.isDeleted) }
                onError(t)
            }
        }
    }

    private fun getCurrentDetailView(): MessageDetailView? {
        return (_messageDetailFlow.value as? DetailViewState.ShowMessage)?.detail
    }

    private fun onError(throwable: Throwable) {
        _messageDetailFlow.value = when (throwable) {
            is MessageNotFoundException -> DetailViewState.Error("Message not found")
            else -> DetailViewState.Error("Internal error")
        }
    }

}