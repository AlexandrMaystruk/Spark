package com.gmail.maystruks08.data

import com.gmail.maystruks08.data.local.MessagesDAO
import com.gmail.maystruks08.data.remote.InboxApi
import com.gmail.maystruks08.domain.entity.Cursor
import com.gmail.maystruks08.domain.entity.GroupedMessages
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.entity.PagedData
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

    override suspend fun loadPagingGrouped(cursor: Cursor?): PagedData<List<GroupedMessages>> {
        val response = inboxService.getInboxMessagesGroupedMock(
            newAfter = cursor?.newAfter,
            pageSize = PAGE_SIZE
        )
        val responseBody = response.body()
        val responseData = responseBody?.data
        if (response.isSuccessful && responseData != null && !responseData.isNullOrEmpty()) {
            responseData.forEach {
                messageDAO.insertAllOrReplace(dataMapper.toDatabaseEntities(it.messages))
            }
        } else {
            //TODO parse exception
            throw Exception("Internal server error")
        }

        val localeMessagesTable = messageDAO.fetchGrouped()
        val localeMessages = localeMessagesTable.map {
            GroupedMessages(
                it.key,
                it.value.take(5).map { dataMapper.toEntity(it) },
                it.value.count()
            )
        }
        return PagedData(
            cursor = responseBody.cursor,
            data = localeMessages
        )
    }

    override suspend fun loadPaged(cursor: Cursor?): PagedData<List<Message>> {
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

        delay(1000)
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

    override suspend fun loadPaged(
        cursor: Cursor?,
        group: String
    ): PagedData<List<Message>> {
        if (!networkUtil.isOnline()) {
            val localeMessagesTable =
                messageDAO.fetchPagedMessages(newAfter = cursor?.newAfter, pageSize = PAGE_SIZE)
            val localeMessages = localeMessagesTable.map { dataMapper.toEntity(it) }
            delay(500)
            val lastElement = localeMessages.lastOrNull()
            val hasNext = lastElement?.id != cursor?.newAfter
            return PagedData(
                cursor = Cursor(hasNext = hasNext, lastElement?.id),
                data = localeMessages
            )
        }

        delay(1000)
        val response = inboxService.getInboxMessagesMock(
            newAfter = cursor?.newAfter,
            pageSize = PAGE_SIZE,
            query = group
        )
        val responseBody = response.body()
        val responseData = responseBody?.data
        if (response.isSuccessful && responseData != null && !responseData.isNullOrEmpty()) {
            messageDAO.insertAllOrReplace(dataMapper.toDatabaseEntities(responseData))
        } else {
            //TODO parse exception
            throw Exception("Internal server error")
        }
        val localeMessagesTable = messageDAO.fetchPagedMessagesByGroup(
            newAfter = cursor?.newAfter,
            pageSize = PAGE_SIZE,
            group = group
        )
        val localeMessages = localeMessagesTable.map { dataMapper.toEntity(it) }
        return PagedData(
            cursor = responseBody.cursor,
            data = localeMessages
        )
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
        const val PAGE_SIZE = 14
    }
}