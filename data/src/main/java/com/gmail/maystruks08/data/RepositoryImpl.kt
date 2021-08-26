package com.gmail.maystruks08.data

import android.util.Log
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

    override suspend fun loadPagingSmartInboxGrouped(cursor: Cursor?): PagedData<List<GroupedMessages>> {
        if (!networkUtil.isOnline()) {
            return fetchPagedSmartInboxFromDB()
        }
        return try {
            fetchPagedSmartInboxFromRemote(cursor?.newAfter)
        } catch (e: Exception) {
            fetchPagedSmartInboxFromDB()
        }
    }

    override suspend fun loadPaged(cursor: Cursor?): PagedData<List<Message>> {
        val newAfter = cursor?.newAfter
        if (!networkUtil.isOnline()) {
            return fetchPagedFromDB(newAfter)
        }
        delay(1000)
        return try {
            fetchPagedFromRemote(newAfter)
        } catch (e: Exception) {
            fetchPagedFromDB(newAfter)
        }
    }

    override suspend fun loadPaged(
        cursor: Cursor?,
        group: String
    ): PagedData<List<Message>> {
        if (!networkUtil.isOnline()) {
            return fetchPagedFromDB(group, cursor?.newAfter)
        }
        return try {
            delay(1000)
            fetchPagedFromRemote(group, cursor?.newAfter)
        } catch (e: Exception) {
            fetchPagedFromDB(group, cursor?.newAfter)
        }
    }

    override suspend fun provideMessageById(messageId: String): Message {
        val messageTable = messageDAO.getMessageById(messageId)
            ?: throw MessageNotFoundException("Message with id:$messageId not found")
        return dataMapper.toEntity(messageTable)
    }

    override suspend fun updateMessage(message: Message) {
        inboxService.mutateMessage(dataMapper.toDtoEntity(message))
        messageDAO.insertOrReplace(dataMapper.toDatabaseEntity(message))
        messageDAO.updateMessageSyncFlag(message.id, true)
    }

    override suspend fun saveNewMessage(message: Message) {
        inboxService.mutateMessage(dataMapper.toDtoEntity(message))
        messageDAO.insertOrReplace(dataMapper.toDatabaseEntity(message))
    }

    //SIMPLE
    private suspend fun fetchPagedFromRemote(newAfter: String?): PagedData<List<Message>> {
        val response = inboxService.getInboxMessagesMock(newAfter = newAfter, pageSize = PAGE_SIZE)
        val responseBody = response.body()
        val responseData = responseBody?.data
        if (response.isSuccessful && responseData != null) {
            messageDAO.insertAllOrReplace(dataMapper.toDatabaseEntities(responseData))
            val messages = responseData.map { dataMapper.toEntity(it) }
            return PagedData(responseBody.cursor, messages)
        } else {
            //TODO parse exception
            throw Exception("Internal server error")

        }
    }

    private suspend fun fetchPagedFromDB(newAfter: String?): PagedData<List<Message>> {
        val localeMessagesTable =
            messageDAO.fetchPagedMessages(newAfter = newAfter, pageSize = PAGE_SIZE)
        val localeMessages = localeMessagesTable.map { dataMapper.toEntity(it) }
        delay(500)
        val databaseCursor = getCursor(localeMessages, newAfter)
        val buffer = StringBuilder().apply {
            localeMessages.forEach { appendLine("id: ${it.id}, date: ${it.date.time}, group: ${it.group}") }
        }
        Log.d("CURSOR", "SIMPLE localeMessages: $buffer")
        return PagedData(
            cursor = databaseCursor,
            data = localeMessages
        )
    }

    //BY GROUP
    private suspend fun fetchPagedFromRemote(
        group: String,
        newAfter: String?
    ): PagedData<List<Message>> {
        val response = inboxService.getInboxMessagesMock(
            newAfter = newAfter,
            pageSize = PAGE_SIZE,
            query = group
        )
        val responseBody = response.body()
        val responseData = responseBody?.data
        if (response.isSuccessful && responseData != null) {
            messageDAO.insertAllOrReplace(dataMapper.toDatabaseEntities(responseData))
            val messages = responseData.map { dataMapper.toEntity(it) }
            return PagedData(responseBody.cursor, messages)
        } else {
            //TODO parse exception
            throw Exception("Internal server error")
        }
    }

    private suspend fun fetchPagedFromDB(
        group: String,
        newAfter: String?
    ): PagedData<List<Message>> {
        val localeMessagesTable = messageDAO.fetchPagedMessagesByGroup(
            newAfter = newAfter,
            pageSize = PAGE_SIZE,
            group = group
        )
        val localeMessages = localeMessagesTable.map { dataMapper.toEntity(it) }
        val buffer = StringBuilder().apply {
            localeMessages.forEach { appendLine("id: ${it.id}, date: ${it.date.time}, group: ${it.group}") }
        }
        Log.d("CURSOR", "BY GROUP localeMessages: $buffer")
        val databaseCursor = getCursor(localeMessages, newAfter)
        return PagedData(
            cursor = databaseCursor,
            data = localeMessages
        )
    }

    //SMART
    private suspend fun fetchPagedSmartInboxFromRemote(newAfter: String?): PagedData<List<GroupedMessages>> {
        val response = inboxService.getInboxMessagesGroupedMock(
            newAfter = newAfter,
            pageSize = PAGE_SIZE
        )
        val responseBody = response.body()
        val responseData = responseBody?.data
        if (response.isSuccessful && responseData != null) {
            responseData.forEach { messageDAO.insertAllOrReplace(dataMapper.toDatabaseEntities(it.messages)) }
            val messagesGroup = responseData.map {
                GroupedMessages(
                    it.group,
                    it.messages.take(5).map { dataMapper.toEntity(it) },
                    it.count
                )
            }
            return PagedData(responseBody.cursor, messagesGroup)
        } else {
            //TODO parse exception
            throw Exception("Internal server error")
        }
    }

    private suspend fun fetchPagedSmartInboxFromDB(): PagedData<List<GroupedMessages>> {
        val localeMessagesTable = messageDAO.fetchGrouped()
        val localeMessages = localeMessagesTable.map {
            GroupedMessages(
                it.key,
                it.value.take(5).map { dataMapper.toEntity(it) },
                it.value.count()
            )
        }
        return PagedData(
            cursor = Cursor(false, null),
            data = localeMessages
        )
    }

    private fun getCursor(messages: List<Message>, previousNewAfter: String?): Cursor {
        val lastElement = messages.lastOrNull()
        val hasNext = if (lastElement != null) lastElement.id != previousNewAfter else false
        val after = if (hasNext) lastElement?.id else null
        Log.d("CURSOR", "hasNext: $hasNext after: $after")
        return Cursor(hasNext = hasNext, newAfter = after)
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}