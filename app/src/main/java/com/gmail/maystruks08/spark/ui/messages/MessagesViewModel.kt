package com.gmail.maystruks08.spark.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.maystruks08.domain.use_cases.ProvideInboxItemsUseCase
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.ui.utils.toView
import com.gmail.maystruks08.spark.ui.utils.view_models.BottomView
import com.gmail.maystruks08.spark.ui.utils.view_models.InboxView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import com.gmail.maystruks08.spark.ui.utils.view_models.StickyView
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessagesViewModel @Inject constructor(
    private val provideInbox: ProvideInboxItemsUseCase
) : ViewModel() {

    val messages get(): StateFlow<List<InboxView>> = _messagesFlow
    private val _messagesFlow = MutableStateFlow<List<InboxView>>(emptyList())

    val showDetailFragment get(): StateFlow<Unit> = _showDetailFlow
    private val _showDetailFlow = MutableStateFlow(Unit)

    init {
        viewModelScope.launch() {
            provideInbox
                .invoke()
                .catch { throwable -> handleError(throwable) }
                .map { inbox ->
                    val resultList = mutableListOf<InboxView>()
                    inbox.forEach { (group, messages) ->
                        resultList.add(StickyView(R.drawable.ic_pin, group))
                        resultList.addAll(messages.map { it.toView() })
                        resultList.add(BottomView("Show all"))
                    }
                    return@map resultList
                }
                .collect {
                    _messagesFlow.value = it
                }
        }
    }

    fun onMessageItemClicked(item: MessageView) {

    }

    fun onMessageItemLongClicked(item: MessageView) {

    }

    fun onShowAllMessageFromGroupClicked(item: BottomView) {

    }

    private fun handleError(throwable: Throwable) {

    }

}