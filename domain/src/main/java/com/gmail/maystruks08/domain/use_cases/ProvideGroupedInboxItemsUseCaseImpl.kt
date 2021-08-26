package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.dispatchers.CoroutineDispatchers
import com.gmail.maystruks08.domain.entity.Cursor
import com.gmail.maystruks08.domain.entity.GroupedMessages
import com.gmail.maystruks08.domain.entity.PagedData
import com.gmail.maystruks08.domain.repositories.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProvideGroupedInboxItemsUseCaseImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val repository: Repository
) : ProvideGroupedInboxItemsUseCase {

    override suspend operator fun invoke(cursor: Cursor?): Flow<PagedData<List<GroupedMessages>>> {
        return flow {
            val pagedData = repository.loadPagingSmartInboxGrouped(cursor)
            val sortedMessages = pagedData.data
            emit(PagedData(pagedData.cursor, sortedMessages))
        }.flowOn(coroutineDispatchers.io())
    }
}
