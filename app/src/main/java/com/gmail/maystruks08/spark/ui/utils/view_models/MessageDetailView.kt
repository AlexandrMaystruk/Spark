package com.gmail.maystruks08.spark.ui.utils.view_models

data class MessageDetailView(
    val id: String,
    val date: String,
    val header: String,
    val from: String,
    val subject: String,
    val content: String,
    var isRead: Boolean,
    var isDeleted: Boolean
)

