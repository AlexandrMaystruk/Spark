package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.PagedData
import com.gmail.maystruks08.domain.entity.Message
import kotlinx.coroutines.flow.Flow

interface ProvidePagingInboxItemsUseCase {

    suspend operator fun invoke(): Flow<PagedData<Map<String, List<Message>>>>

}

