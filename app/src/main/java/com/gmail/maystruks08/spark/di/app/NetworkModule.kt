package com.gmail.maystruks08.spark.di.app

import com.gmail.maystruks08.data.NetworkUtil
import com.gmail.maystruks08.data.RepositoryImpl
import com.gmail.maystruks08.domain.repositories.Repository
import com.gmail.maystruks08.domain.use_cases.NewMessageReceivedUseCase
import com.gmail.maystruks08.domain.use_cases.NewMessageReceivedUseCaseImpl
import com.gmail.maystruks08.spark.NetworkUtilImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface NetworkModule {

    @Binds
    @Singleton
    fun bindNetworkUtil(impl: NetworkUtilImpl): NetworkUtil


    @Binds
    @Singleton
    fun bindNewMessageReceivedUseCase(impl: NewMessageReceivedUseCaseImpl): NewMessageReceivedUseCase

    @Binds
    @Singleton
    fun bindRepository(impl: RepositoryImpl): Repository

}