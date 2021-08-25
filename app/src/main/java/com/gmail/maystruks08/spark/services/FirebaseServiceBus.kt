package com.gmail.maystruks08.spark.services

import com.gmail.maystruks08.domain.entity.Message
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseServiceBus @Inject constructor() {

    private val newMessageChannel = Channel<Message>()

    suspend fun sendEvent(message: Message) {
        newMessageChannel.send(message)
    }

    fun subscribeToEvent(): Channel<Message> {
        return newMessageChannel
    }

}