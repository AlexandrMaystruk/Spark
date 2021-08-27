package com.gmail.maystruks08.domain.entity

import java.util.*

data class Message(
    val id: String,
    val date: Date,
    val from: String,
    val subject: String,
    val header: String,
    val contentPreview: String,
    val content: String,
    val group: String,
    var isRead: Boolean,
    var isDeleted: Boolean
)