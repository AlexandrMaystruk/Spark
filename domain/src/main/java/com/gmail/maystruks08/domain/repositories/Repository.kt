package com.gmail.maystruks08.domain.repositories

import androidx.paging.PagingSource
import com.gmail.maystruks08.domain.PagedData
import com.gmail.maystruks08.domain.entity.Message

interface Repository {

    suspend fun loadPaging(
        page: Int? = null,
        pageSize: Int? = null
    ): PagedData<List<Message>>

    suspend fun provideInboxData(): List<Message>

    suspend fun provideMessageById(messageId: String): Message

    suspend fun updateMessage(message: Message)

    suspend fun saveNewMessage(message: Message)

}