package com.gmail.maystruks08.spark

import android.app.Application
import android.content.Context
import com.gmail.maystruks08.spark.di.app.AppComponent
import com.gmail.maystruks08.spark.di.app.AppModule
import com.gmail.maystruks08.spark.di.app.DaggerAppComponent
import com.gmail.maystruks08.spark.di.inbox.message_list.MessageListComponent
import com.gmail.maystruks08.spark.di.inbox.detail.DetailComponent

class App : Application() {

    companion object {

        lateinit var appComponent: AppComponent

        var messagesComponent: MessageListComponent? = null
            get() {
                if (field == null) field = appComponent.provideMessageListComponent()
                return field
            }

        var detailComponent: DetailComponent? = null
            get() {
                if (field == null) field = appComponent.provideDetailComponent()
                return field
            }

        fun clearMessageListComponent() {
            messagesComponent = null
        }

        fun clearDetailComponent() {
            detailComponent = null
        }
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
            .apply { inject(this@App) }
    }
}