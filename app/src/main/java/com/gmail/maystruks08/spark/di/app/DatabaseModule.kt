package com.gmail.maystruks08.spark.di.app

import android.content.Context
import com.gmail.maystruks08.data.local.AppDatabase
import com.gmail.maystruks08.data.local.MessagesDAO
import dagger.Module
import dagger.Provides
import androidx.room.Room
import javax.inject.Singleton

@Module
object DatabaseModule{

    @Provides
    @Singleton
    fun appDatabase(context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "inbox_db").build()

    @Provides
    @Singleton
    fun  provideRaceDao(appDatabase: AppDatabase): MessagesDAO = appDatabase.messagesDao()

}