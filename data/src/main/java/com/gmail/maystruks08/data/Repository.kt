package com.gmail.maystruks08.data

import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.domain.entity.exceptions.MessageNotFoundException
import com.gmail.maystruks08.domain.repositories.Repository
import java.util.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {


    companion object {

        private val inbox = listOf(
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Alex",
                subject = "Andrey",
                contentPreview = "I hope my test app is not bad=)",
                group = "Others",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Others",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Others",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Alex",
                subject = "Andrey",
                contentPreview = "I hope my test app is not bad=)",
                group = "Personal",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Notification",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Notification",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Notification",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Notification",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                isRead = false,
                isDeleted = false
            ),
            Message(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                isRead = false,
                isDeleted = false
            ),
        )
    }

    override suspend fun provideInboxData(): List<Message> {
        return inbox
    }

    override suspend fun provideMessageById(messageId: String): Message {
        return inbox
            .firstOrNull { it.id == messageId }
            ?: throw MessageNotFoundException("Message with id:$messageId not found")
    }
}