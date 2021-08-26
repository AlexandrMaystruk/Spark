package com.gmail.maystruks08.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MessagesDAO : BaseDao<MessageTable> {

    @Query("SELECT* FROM messages")
    suspend fun fetchAllMessages(): List<MessageTable>

    @Query("SELECT* FROM messages WHERE messageGroup =:group")
    suspend fun fetchAllMessagesByGroup(group: String): List<MessageTable>

    @Query("SELECT * FROM messages WHERE date < COALESCE((SELECT date FROM messages WHERE id =:newAfter), CURRENT_DATE) ORDER BY date DESC LIMIT :pageSize")
    suspend fun fetchPagedMessages(newAfter: String?, pageSize: Int): List<MessageTable>

    @Query("SELECT * FROM messages WHERE date < COALESCE((SELECT date FROM messages WHERE id =:newAfter AND messageGroup LIKE :group), CURRENT_DATE) ORDER BY date DESC LIMIT :pageSize")
    suspend fun fetchPagedMessagesByGroup(newAfter: String?, pageSize: Int, group: String): List<MessageTable>

    @Query("SELECT* FROM messages WHERE id =:messageId")
    suspend fun getMessageById(messageId: String): MessageTable?

    @Query("UPDATE messages SET isNeedToSync =:isNeedToSync WHERE id =:messageId")
    suspend fun updateMessageSyncFlag(messageId: String, isNeedToSync: Boolean)

    @Query("SELECT * FROM messages")
    fun getPagedMessages(): DataSource.Factory<Int, MessageTable>

    @Query("SELECT DISTINCT messageGroup FROM messages ")
    suspend fun fetchAllMessageGroups(): List<String>

    @Transaction
    suspend fun fetchGrouped(): MutableMap<String, List<MessageTable>> {
        val result = mutableMapOf<String, List<MessageTable>>()
        val groups = fetchAllMessageGroups()
        groups.forEach {
            result[it] = fetchAllMessagesByGroup(it)
        }
        return result
    }

}


