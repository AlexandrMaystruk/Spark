package com.gmail.maystruks08.domain.repositories

import com.gmail.maystruks08.domain.entity.Message

interface Repository{

    suspend fun provideInboxData(): List<Message>

    suspend fun provideMessageById(messageId: String): Message

    suspend fun updateMessage(message: Message)

}