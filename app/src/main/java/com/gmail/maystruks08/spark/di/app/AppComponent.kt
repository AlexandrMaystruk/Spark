package com.gmail.maystruks08.spark.di.app

import android.content.Context
import android.content.res.Resources
import com.gmail.maystruks08.spark.App
import com.gmail.maystruks08.spark.MainActivity
import com.gmail.maystruks08.spark.di.inbox.detail.DetailComponent
import com.gmail.maystruks08.spark.di.inbox.message_list.MessageListComponent
import com.gmail.maystruks08.spark.services.FCMNotificationServiceMock
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, NetworkModule::class, APIModule::class, CoroutineModule::class])
interface AppComponent {

    fun provideAppContext(): Context

    fun provideResources(): Resources

    fun provideMessageListComponent(): MessageListComponent

    fun provideDetailComponent(): DetailComponent

    fun inject(app: App)

    fun inject(app: MainActivity)

    fun inject(app: FCMNotificationServiceMock)
}