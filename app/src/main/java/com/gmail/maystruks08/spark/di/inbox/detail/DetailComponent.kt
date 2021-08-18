package com.gmail.maystruks08.spark.di.inbox.detail

import com.gmail.maystruks08.spark.ui.detail.DetailFragment
import dagger.Subcomponent

@Subcomponent(modules = [DetailModule::class])
@DetailScope

interface DetailComponent {

    fun inject(fragment: DetailFragment)

}