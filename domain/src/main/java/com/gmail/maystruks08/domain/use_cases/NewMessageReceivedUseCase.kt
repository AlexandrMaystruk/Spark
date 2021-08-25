package com.gmail.maystruks08.domain.use_cases

import com.gmail.maystruks08.domain.entity.Message

interface NewMessageReceivedUseCase {

    suspend operator fun invoke(newMessage: Message)

}
