package com.gmail.maystruks08.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query

@Dao
interface MessagesDAO : BaseDao<MessageTable> {

    @Query("SELECT* FROM messages")
    suspend fun fetchMessages(): List<MessageTable>


    @Query("SELECT* FROM messages ")
    suspend fun fetchPagedMessages(): List<MessageTable>

    @Query("SELECT* FROM messages WHERE id =:messageId")
    suspend fun getMessageById(messageId: String): MessageTable?

    @Query("UPDATE messages SET isNeedToSync =:isNeedToSync WHERE id =:messageId")
    suspend fun updateMessageSyncFlag(messageId: String, isNeedToSync: Boolean)

    @Query("SELECT * FROM messages")
    fun getPagedMessages(): DataSource.Factory<Int, MessageTable>

}


