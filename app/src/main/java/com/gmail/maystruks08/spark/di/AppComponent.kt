package com.gmail.maystruks08.spark.di

import android.content.Context
import android.content.res.Resources
import com.gmail.maystruks08.spark.App
import com.gmail.maystruks08.spark.di.inbox.MessageListComponent
import com.gmail.maystruks08.spark.di.detail.DetailComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, NetworkModule::class, APIModule::class])
interface AppComponent {

    fun provideAppContext(): Context

    fun provideResources(): Resources

    fun provideMessageListComponent(): MessageListComponent

    fun provideDetailComponent(): DetailComponent

    fun inject(app: App)
}