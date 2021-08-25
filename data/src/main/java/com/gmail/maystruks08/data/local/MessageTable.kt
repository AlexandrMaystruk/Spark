package com.gmail.maystruks08.data.local

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "messages",
    primaryKeys = ["id"],
    indices = [Index(value = arrayOf("id"))]
)
data class MessageTable(
    val id: String,
    val from: String,
    val subject: String,
    val contentPreview: String,
    val content: String,
    val isRead: Boolean,
    val isDeleted: Boolean,
    val isNeedToSync: Boolean = false
)