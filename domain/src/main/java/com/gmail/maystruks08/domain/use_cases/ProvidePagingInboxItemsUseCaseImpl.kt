package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.Cursor
import com.gmail.maystruks08.domain.PagedData
import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchers
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.repositories.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProvidePagingInboxItemsUseCaseImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val repository: Repository
) : ProvidePagingInboxItemsUseCase {

    override suspend fun invoke(cursor: Cursor?): Flow<PagedData<Map<String, List<Message>>>> {
        return flow {
            val pagedData = repository.loadPaging(cursor)
            val sortedMessages = pagedData.data
                .asSequence()
                .filter { !it.isDeleted }
                .sortedBy { it.date }
                .toList()
                .groupBy { it.group }
            emit(PagedData(pagedData.cursor, sortedMessages))
        }.flowOn(coroutineDispatchers.io())
    }
}