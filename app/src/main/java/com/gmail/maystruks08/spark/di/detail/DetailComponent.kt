package com.gmail.maystruks08.spark.di.detail

import com.gmail.maystruks08.spark.ui.detail.DetailFragment
import dagger.Subcomponent

@Subcomponent(modules = [DetailModule::class])
@DetailScope

interface DetailComponent {

    fun inject(fragment: DetailFragment)

}