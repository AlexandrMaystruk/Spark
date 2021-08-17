package com.gmail.maystruks08.spark.di

import com.gmail.maystruks08.data.RepositoryImpl
import com.gmail.maystruks08.domain.repositories.Repository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
interface APIModule {

    @Binds
    @Singleton
    fun bindRepository(impl: RepositoryImpl): Repository

}