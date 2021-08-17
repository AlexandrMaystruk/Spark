package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.entity.Message
import kotlinx.coroutines.flow.Flow

interface ProvideInboxItemsUseCase {

    suspend operator fun invoke(): Flow<Map<String, List<Message>>>

}