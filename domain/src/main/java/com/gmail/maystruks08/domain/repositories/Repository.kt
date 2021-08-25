package com.gmail.maystruks08.domain.repositories

import com.gmail.maystruks08.domain.entity.Cursor
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.entity.PagedData

interface Repository {

    suspend fun loadPaging(cursor: Cursor?): PagedData<List<Message>>

    suspend fun provideInboxData(group: String): List<Message>

    suspend fun provideMessageById(messageId: String): Message

    suspend fun updateMessage(message: Message)

    suspend fun saveNewMessage(message: Message)

}