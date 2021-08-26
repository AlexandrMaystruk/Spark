package com.gmail.maystruks08.domain.repositories

import com.gmail.maystruks08.domain.entity.Cursor
import com.gmail.maystruks08.domain.entity.GroupedMessages
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.entity.PagedData

interface Repository {

    suspend fun loadPagingGrouped(cursor: Cursor?): PagedData<List<GroupedMessages>>

    suspend fun loadPaged(cursor: Cursor?): PagedData<List<Message>>

    suspend fun loadPaged(cursor: Cursor?, group: String): PagedData<List<Message>>

    suspend fun provideMessageById(messageId: String): Message

    suspend fun updateMessage(message: Message)

    suspend fun saveNewMessage(message: Message)

}