package com.gmail.maystruks08.spark.ui.utils

import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageDetailView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView

fun Message.toView(): MessageView {
    return MessageView(id, date.toString(), subject)
}

fun Message.toDetailView(): MessageDetailView {
    return MessageDetailView(id, date.time, from, subject, contentPreview, isRead, isDeleted)
}