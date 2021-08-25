package com.gmail.maystruks08.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(obj: T)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOrReplace(obj: List<T>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(obj: List<T>)

}