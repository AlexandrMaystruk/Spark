package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchers
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.repositories.Repository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewMessageReceivedUseCaseImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val repository: Repository
) : NewMessageReceivedUseCase {

    override suspend fun invoke(newMessage: Message) {
        withContext(coroutineDispatchers.io()) {
            repository.saveNewMessage(newMessage)
        }
    }
}
