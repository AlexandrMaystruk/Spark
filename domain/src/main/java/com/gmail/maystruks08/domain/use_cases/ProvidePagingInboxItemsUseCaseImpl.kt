package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.PagedData
import com.gmail.maystruks08.domain.entity.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProvidePagingInboxItemsUseCaseImpl @Inject constructor() : ProvidePagingInboxItemsUseCase {

    override suspend operator fun invoke(): Flow<PagedData<Map<String, List<Message>>>> {
        return flow {
            emit(PagedData(hasNext = true, hasPrevious = false, data = mutableMapOf()))
        }
    }
}