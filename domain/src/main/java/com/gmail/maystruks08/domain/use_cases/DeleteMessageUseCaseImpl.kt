package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.repositories.Repository
import javax.inject.Inject

class DeleteMessageUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val provideMessageUseCase: ProvideMessageUseCase
) : DeleteMessageUseCase {

    override suspend operator fun invoke(messageId: String): Message {
        val message = provideMessageUseCase.invoke(messageId)
        message.isDeleted = true
        repository.updateMessage(message)
        return message
    }
}