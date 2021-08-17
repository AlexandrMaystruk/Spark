package com.gmail.maystruks08.spark.ui.utils.view_models

data class MessageDetailView(
    val id: String,
    val date: Long,
    val from: String,
    val subject: String,
    val content: String,
    val isRead: Boolean,
    val isDeleted: Boolean
)

