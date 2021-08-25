package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.entity.Cursor
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.entity.PagedData
import kotlinx.coroutines.flow.Flow

interface ProvidePagingInboxItemsUseCase {

    suspend operator fun invoke(cursor: Cursor?): Flow<PagedData<Map<String, List<Message>>>>

}

