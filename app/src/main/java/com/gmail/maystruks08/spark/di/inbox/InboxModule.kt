package com.gmail.maystruks08.spark.di.inbox

import com.gmail.maystruks08.domain.use_cases.*
import dagger.Binds
import dagger.Module

@Module
interface InboxModule {

    @Binds
    fun bindProvideMessageUseCase(impl: ProvideMessageUseCaseImpl): ProvideMessageUseCase

    @Binds
    fun bindDeleteMessageUseCase(impl: DeleteMessageUseCaseImpl): DeleteMessageUseCase

    @Binds
    fun bindSwitchReadReadMessageStateUseCase(impl: SwitchReadReadMessageStateUseCaseImpl): SwitchReadReadMessageStateUseCase

    @Binds
    fun bindProvidePagingInboxItemsUseCase(impl: ProvidePagingInboxItemsUseCaseImpl): ProvidePagingInboxItemsUseCase
}