package com.gmail.maystruks08.data

import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.repositories.Repository
import java.util.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {

    override suspend fun provideInboxData(): List<Message> {
        return listOf(
            Message(
                date = Date(),
                from = "Alex",
                subject = "Andrey",
                contentPreview = "I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            Message(
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                isRead = false,
                isDeleted = false
            )
        )
    }
}