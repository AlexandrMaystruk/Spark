package com.gmail.maystruks08.spark.ui.utils

import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView

fun  Message.toView(): MessageView{
    return MessageView(date.toString(), subject)
}