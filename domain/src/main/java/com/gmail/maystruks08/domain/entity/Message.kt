package com.gmail.maystruks08.domain.entity

import java.util.*

data class Message(
    val date: Date,
    val from: String,
    val subject: String,
    val contentPreview: String,
    val isRead: Boolean,
    val isDeleted: Boolean
)
