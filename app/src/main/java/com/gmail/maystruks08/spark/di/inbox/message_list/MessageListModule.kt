package com.gmail.maystruks08.spark.di.inbox.message_list

import androidx.lifecycle.ViewModel
import com.gmail.maystruks08.domain.use_cases.ProvideGroupedInboxItemsUseCase
import com.gmail.maystruks08.domain.use_cases.ProvideGroupedInboxItemsUseCaseImpl
import com.gmail.maystruks08.domain.use_cases.ProvideInboxItemsUseCase
import com.gmail.maystruks08.domain.use_cases.ProvideInboxItemsUseCaseImpl
import com.gmail.maystruks08.spark.di.base.ViewModelKey
import com.gmail.maystruks08.spark.di.base.ViewModelModule
import com.gmail.maystruks08.spark.di.inbox.InboxModule
import com.gmail.maystruks08.spark.ui.messages.MessagesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class, InboxModule::class])
abstract class MessageListModule {

    @IntoMap
    @Binds
    @MessageListScope
    @ViewModelKey(MessagesViewModel::class)
    abstract fun bindViewModel(viewModel: MessagesViewModel): ViewModel

    @Binds
    @MessageListScope
    abstract fun bindProvideInboxItemsUseCase(impl: ProvideInboxItemsUseCaseImpl): ProvideInboxItemsUseCase

    @Binds
    @MessageListScope
    abstract fun bindProvideGroupedInboxItemsUseCase(impl: ProvideGroupedInboxItemsUseCaseImpl): ProvideGroupedInboxItemsUseCase

}