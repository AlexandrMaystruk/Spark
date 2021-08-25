package com.gmail.maystruks08.data

import com.gmail.maystruks08.data.local.MessagesDAO
import com.gmail.maystruks08.data.remote.InboxApi
import com.gmail.maystruks08.domain.Cursor
import com.gmail.maystruks08.domain.PagedData
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.entity.exceptions.MessageNotFoundException
import com.gmail.maystruks08.domain.repositories.Repository
import kotlinx.coroutines.delay
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val networkUtil: NetworkUtil,
    private val inboxService: InboxApi,
    private val messageDAO: MessagesDAO,
    private val dataMapper: DataMapper
) : Repository {

    override suspend fun loadPaging(cursor: Cursor?): PagedData<List<Message>> {
        if (!networkUtil.isOnline()) {
            val localeMessagesTable = messageDAO.fetchPagedMessages(newAfter = cursor?.newAfter, pageSize = PAGE_SIZE)
            val localeMessages = localeMessagesTable.map { dataMapper.toEntity(it) }
            delay(500)
            val lastElement = localeMessages.lastOrNull()
            val hasNext = lastElement?.id != cursor?.newAfter
            return PagedData(
                cursor = Cursor(hasNext = hasNext, lastElement?.id),
                data = localeMessages
            )
        }

        delay(2000)
        val response = inboxService.getInboxMessagesMock(newAfter = cursor?.newAfter, pageSize = PAGE_SIZE)
        val responseBody = response.body()
        val responseData = responseBody?.data
        if (response.isSuccessful && responseData != null && !responseData.isNullOrEmpty()) {
            messageDAO.insertAllOrReplace(dataMapper.toDatabaseEntities(responseData))
        } else {
            //TODO parse exception
            throw Exception("Internal server error")
        }

        val localeMessagesTable = messageDAO.fetchPagedMessages(newAfter = cursor?.newAfter, pageSize = PAGE_SIZE)
        val localeMessages = localeMessagesTable.map { dataMapper.toEntity(it) }
        return PagedData(
            cursor = responseBody.cursor,
            data = localeMessages
        )
    }

    override suspend fun provideInboxData(): List<Message> {
        val localeMessages = messageDAO.fetchAllMessages()
        return localeMessages.map { dataMapper.toEntity(it) }
    }

    override suspend fun provideMessageById(messageId: String): Message {
        val messageTable = messageDAO.getMessageById(messageId)
            ?: throw MessageNotFoundException("Message with id:$messageId not found")
        return dataMapper.toEntity(messageTable)
    }

    override suspend fun updateMessage(message: Message) {
        messageDAO.insertOrReplace(dataMapper.toDatabaseEntity(message))
        messageDAO.updateMessageSyncFlag(message.id, true)
    }

    override suspend fun saveNewMessage(message: Message) {
        val tableEntity = dataMapper.toDatabaseEntity(message)
        messageDAO.insertOrReplace(tableEntity)
    }

    companion object {
        const val PAGE_SIZE = 8
    }
}