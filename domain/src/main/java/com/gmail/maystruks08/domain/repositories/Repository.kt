package com.gmail.maystruks08.domain.repositories

import com.gmail.maystruks08.domain.Cursor
import com.gmail.maystruks08.domain.PagedData
import com.gmail.maystruks08.domain.entity.Message

interface Repository {

    suspend fun loadPaging(cursor: Cursor?): PagedData<List<Message>>

    suspend fun provideInboxData(): List<Message>

    suspend fun provideMessageById(messageId: String): Message

    suspend fun updateMessage(message: Message)

    suspend fun saveNewMessage(message: Message)

}