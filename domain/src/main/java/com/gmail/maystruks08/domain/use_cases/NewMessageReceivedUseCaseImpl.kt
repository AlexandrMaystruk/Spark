package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.repositories.Repository
import javax.inject.Inject

class NewMessageReceivedUseCaseImpl @Inject constructor(
    private val repository: Repository
) : NewMessageReceivedUseCase {

    override suspend fun invoke(newMessage: Message) {
        repository.saveNewMessage(newMessage)
    }
}
