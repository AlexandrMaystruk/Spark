package com.gmail.maystruks08.spark.di

import android.content.Context
import android.content.res.Resources
import com.gmail.maystruks08.data.RepositoryImpl
import com.gmail.maystruks08.domain.repositories.Repository
import com.gmail.maystruks08.spark.di.inbox.MessageListScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context.applicationContext

    @Provides
    fun provideResources(): Resources = context.resources

}