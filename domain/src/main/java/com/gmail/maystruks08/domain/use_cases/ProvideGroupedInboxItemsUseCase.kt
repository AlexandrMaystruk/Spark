package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.entity.Cursor
import com.gmail.maystruks08.domain.entity.GroupedMessages
import com.gmail.maystruks08.domain.entity.PagedData
import kotlinx.coroutines.flow.Flow

interface ProvideGroupedInboxItemsUseCase {

    suspend operator fun invoke(cursor: Cursor?): Flow<PagedData<List<GroupedMessages>>>

}


