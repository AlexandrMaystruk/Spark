package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchers
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.repositories.Repository
import javax.inject.Inject

class ProvideMessageUseCaseImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val repository: Repository
) : ProvideMessageUseCase {

    override suspend operator fun invoke(messageId: String): Message {
        return repository.provideMessageById(messageId)
    }

}