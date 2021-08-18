package com.gmail.maystruks08.data.local

import androidx.room.Entity
import androidx.room.Index
import java.util.*

@Entity(
    tableName = "messages",
    primaryKeys = ["id"],
    indices = [Index(value = arrayOf("id"))]
)
data class MessageTable(
    val id: String,
    val from: Date,
    val subject: String,
    val contentPreview: String,
    val isRead: Boolean,
    val isDeleted: Boolean
)