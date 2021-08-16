package com.gmail.maystruks08.spark.di.inbox

import com.gmail.maystruks08.spark.ui.messages.MessagesFragment
import dagger.Subcomponent

@Subcomponent(modules = [MessageListModule::class])
@MessageListScope

interface MessageListComponent {

    fun inject(fragment: MessagesFragment)

}