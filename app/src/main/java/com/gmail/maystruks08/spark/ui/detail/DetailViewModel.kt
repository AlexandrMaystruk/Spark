package com.gmail.maystruks08.spark.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.maystruks08.domain.use_cases.ProvideMessageUseCase
import com.gmail.maystruks08.spark.ui.messages.InboxViewMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val inboxViewMapper: InboxViewMapper,
    private val provideMessageUseCase: ProvideMessageUseCase
) : ViewModel() {

    val selectedMessage get() = _messagesFlow
    private val _messagesFlow = MutableStateFlow<DetailViewState>(DetailViewState.Loading)

    fun requireMessage(messageId: String) {
        viewModelScope.launch {
            try {
                val message = provideMessageUseCase.invoke(messageId)
                val detailView = inboxViewMapper.toDetailView(message)
                _messagesFlow.value = DetailViewState.ShowMessage(detailView)
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    private fun onError(throwable: Throwable) {

    }

}