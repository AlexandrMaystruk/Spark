package com.gmail.maystruks08.data.pojo

import java.util.*

data class MessageDto(
    val id: String,
    val date: Date,
    val from: String,
    val subject: String,
    val contentPreview: String,
    val group: String,
    val content: String,
    val isRead: Boolean,
    val isDeleted: Boolean
)
