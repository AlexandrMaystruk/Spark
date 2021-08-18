package com.gmail.maystruks08.spark.di.app

import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchers
import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
interface CoroutineModule {

    @Binds
    @Singleton
    fun bindDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers

}