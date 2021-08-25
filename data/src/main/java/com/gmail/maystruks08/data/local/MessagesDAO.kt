package com.gmail.maystruks08.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query

@Dao
interface MessagesDAO : BaseDao<MessageTable> {

    @Query("SELECT* FROM messages")
    suspend fun fetchAllMessages(): List<MessageTable>

    @Query("SELECT* FROM messages WHERE messageGroup =:group")
    suspend fun fetchAllMessagesByGroup(group: String): List<MessageTable>

    @Query("SELECT * FROM messages WHERE date < COALESCE((SELECT date FROM messages WHERE id =:newAfter), CURRENT_DATE) ORDER BY date DESC LIMIT :pageSize")
    suspend fun fetchPagedMessages(newAfter: String?, pageSize: Int): List<MessageTable>

    @Query("SELECT* FROM messages WHERE id =:messageId")
    suspend fun getMessageById(messageId: String): MessageTable?

    @Query("UPDATE messages SET isNeedToSync =:isNeedToSync WHERE id =:messageId")
    suspend fun updateMessageSyncFlag(messageId: String, isNeedToSync: Boolean)

    @Query("SELECT * FROM messages")
    fun getPagedMessages(): DataSource.Factory<Int, MessageTable>

}


