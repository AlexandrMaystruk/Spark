package com.gmail.maystruks08.spark.di.app

import com.gmail.maystruks08.data.BASE_URL
import com.gmail.maystruks08.data.remote.InboxApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
object APIModule {

    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun retrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

//    @Provides
//    @Singleton
//    fun provideApi(retrofit: Retrofit): InboxApi = retrofit.create(InboxApi::class.java)

}