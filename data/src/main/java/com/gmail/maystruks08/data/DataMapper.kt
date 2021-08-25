package com.gmail.maystruks08.data

import com.gmail.maystruks08.data.local.MessageTable
import com.gmail.maystruks08.data.pojo.MessageDto
import com.gmail.maystruks08.domain.entity.Message
import javax.inject.Inject

class DataMapper @Inject constructor() {

    fun toDatabaseEntities(messageDtoList: List<MessageDto>): List<MessageTable> {
        return messageDtoList.map { toDatabaseEntity(it) }
    }

    fun toDatabaseEntity(messageDto: MessageDto): MessageTable {
        return messageDto.run {
            MessageTable(
                id = id,
                from = from,
                subject = subject,
                contentPreview = contentPreview,
                content = content,
                isRead = isRead,
                isDeleted = isDeleted,
                group = group,
                date = date
            )
        }
    }

    fun toDatabaseEntity(message: Message): MessageTable {
        return message.run {
            MessageTable(
                id = id,
                from = from,
                subject = subject,
                contentPreview = contentPreview,
                content = content,
                isRead = isRead,
                isDeleted = isDeleted,
                group = group,
                date = date
            )
        }
    }


    fun toEntity(messageDto: MessageDto): Message {
        return messageDto.run {
            Message(
                id = id,
                from = from,
                subject = subject,
                contentPreview = contentPreview,
                content = content,
                isRead = isRead,
                isDeleted = isDeleted,
                group = group,
                date = date
            )
        }
    }


    fun toEntity(messageDto: MessageTable): Message {
        return messageDto.run {
            Message(
                id = id,
                from = from,
                subject = subject,
                contentPreview = contentPreview,
                content = content,
                isRead = isRead,
                isDeleted = isDeleted,
                group = group,
                date = date
            )
        }
    }

    fun mapApiException(t: Throwable): Exception {
        return RuntimeException()
    }

}