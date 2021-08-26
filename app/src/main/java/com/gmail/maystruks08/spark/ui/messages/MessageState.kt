package com.gmail.maystruks08.spark.ui.messages

import com.gmail.maystruks08.spark.ui.utils.view_models.InboxView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView

sealed class MessageState {
    object Loading : MessageState()
    class ShowInboxList(val data: List<InboxView>) : MessageState()
    class Error(val message: String) : MessageState()
}

sealed class NavigationState {
    object Nothing : NavigationState()
    class OpenDetailScreen(val item: MessageView) : NavigationState()
}


sealed class InboxMode {
    object Smart : InboxMode()
    object Simple : InboxMode()
    data class Group(val name: String) : InboxMode()
}