package com.gmail.maystruks08.data.pojo

import java.util.*

data class MessagePojo(
    val date: Date,
    val from: Date,
    val subject: String,
    val contentPreview: String,
    val isRead: Boolean,
    val isDeleted: Boolean
)
