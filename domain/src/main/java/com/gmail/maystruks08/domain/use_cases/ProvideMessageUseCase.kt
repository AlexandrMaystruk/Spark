package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.entity.Message

interface ProvideMessageUseCase {

    suspend operator fun invoke(messageId: String): Message

}