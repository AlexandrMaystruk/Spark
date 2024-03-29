package com.gmail.maystruks08.spark.di.inbox.detail

import androidx.lifecycle.ViewModel
import com.gmail.maystruks08.spark.di.base.ViewModelKey
import com.gmail.maystruks08.spark.di.base.ViewModelModule
import com.gmail.maystruks08.spark.di.inbox.InboxModule
import com.gmail.maystruks08.spark.ui.detail.DetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelModule::class, InboxModule::class])
abstract class DetailModule {

    @IntoMap
    @Binds
    @DetailScope
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindViewModel(viewModel: DetailViewModel): ViewModel

}