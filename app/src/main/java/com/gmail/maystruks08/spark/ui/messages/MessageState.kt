package com.gmail.maystruks08.spark.ui.messages

import com.gmail.maystruks08.spark.ui.utils.view_models.InboxView

sealed class MessageState {
    object Loading : MessageState()
    class ShowInboxList(val data: List<InboxView>) : MessageState()
    class Error(val message: String) : MessageState()
}
