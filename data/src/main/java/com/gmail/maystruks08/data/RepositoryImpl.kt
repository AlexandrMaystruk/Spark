package com.gmail.maystruks08.data

import com.gmail.maystruks08.data.local.MessagesDAO
import com.gmail.maystruks08.data.remote.InboxApi
import com.gmail.maystruks08.data.remote.data_source.MessagePageDataSource
import com.gmail.maystruks08.domain.PagedData
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.entity.exceptions.MessageNotFoundException
import com.gmail.maystruks08.domain.repositories.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val networkUtil: NetworkUtil,
    private val messagePageDataSource: MessagePageDataSource,
    private val inboxService: InboxApi,
    private val messageDAO: MessagesDAO,
    private val dataMapper: DataMapper
) : Repository {

    override suspend fun loadPaging(page: Int?, pageSize: Int?): PagedData<List<Message>> {
        val response = inboxService.getInboxMessagesMock(page = page, pageSize = pageSize)
        val responseBody = response.body()
        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            messageDAO.insertAllOrReplace(dataMapper.toDatabaseEntities(responseBody))
        } else {
            throw Exception()
        }
        val localeMessages = messageDAO.fetchPagedMessages()
        return PagedData(
            hasNext = true,
            hasPrevious = false,
            data = localeMessages.map { dataMapper.toEntity(it) })
    }

    override suspend fun provideInboxData(): List<Message> {
        if (networkUtil.isOnline()) {
            val response = inboxService.getInboxMessagesMock(page = null, pageSize = null)
            val responseBody = response.body()
            if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                messageDAO.insertAllOrReplace(dataMapper.toDatabaseEntities(responseBody))
            } else {
                throw Exception()
            }
        }
        val localeMessages = messageDAO.fetchMessages()
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
}