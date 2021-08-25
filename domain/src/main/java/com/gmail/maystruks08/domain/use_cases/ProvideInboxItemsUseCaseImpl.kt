package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchers
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.repositories.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProvideInboxItemsUseCaseImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val repository: Repository
) : ProvideInboxItemsUseCase {

    override suspend operator fun invoke(): Flow<Map<String, List<Message>>> {
        return flow {
            val sortedMessages = repository.provideInboxData()
                .asSequence()
                .filter { !it.isDeleted }
                .sortedBy { it.date }
                .toList()
                .groupBy { it.group }
            emit(sortedMessages)
        }.flowOn(coroutineDispatchers.io())
    }
}