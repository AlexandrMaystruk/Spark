package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchers
import com.gmail.maystruks08.domain.entity.Cursor
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.entity.PagedData
import com.gmail.maystruks08.domain.repositories.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProvideInboxItemsUseCaseImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val repository: Repository
) : ProvideInboxItemsUseCase {

    override suspend operator fun invoke(
        group: String,
        cursor: Cursor?
    ): Flow<PagedData<List<Message>>> {
        return flow {
            val messages = repository.loadPaged(cursor, group)
            emit(messages)
        }.flowOn(coroutineDispatchers.io())
    }
}