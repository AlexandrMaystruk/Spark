package com.gmail.maystruks08.spark.di.inbox

import com.gmail.maystruks08.domain.use_cases.*
import dagger.Binds
import dagger.Module

@Module
abstract class InboxModule {

    @Binds
    abstract fun bindProvideMessageUseCase(impl: ProvideMessageUseCaseImpl): ProvideMessageUseCase

    @Binds
    abstract fun bindDeleteMessageUseCase(impl: DeleteMessageUseCaseImpl): DeleteMessageUseCase

    @Binds
    abstract fun bindSwitchReadReadMessageStateUseCase(impl: SwitchReadReadMessageStateUseCaseImpl): SwitchReadReadMessageStateUseCase
}