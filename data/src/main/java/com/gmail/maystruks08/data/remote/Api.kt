package com.gmail.maystruks08.data.remote

import com.gmail.maystruks08.data.pojo.MessageDto
import com.gmail.maystruks08.domain.Cursor
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject


class InboxApi @Inject constructor() {

//    @GET("/inboxList.json")
//    fun getInboxMessageList(
//        @Query("apiKey") apiKey: String? = null,
//        @Query("page") page: Int? = null,
//        @Query("pageSize") pageSize: Int? = null,
//        @Query("q") source: String? = null
//    ): Response<List<MessageDto>>


    fun getInboxMessagesMock(
        newAfter: String? = null,
        pageSize: Int
    ): Response<ApiResponse> {
        var hasNext = true
        val after: String?
        //init
        if (newAfter == null) {
            val lastIndex = if (pageSize > inbox.lastIndex) {
                hasNext = false
                inbox.lastIndex
            } else {
                hasNext = true
                pageSize
            }
            val pagedList = inbox.subList(0, lastIndex)
            after = pagedList.lastOrNull()?.id
            val cursor = Cursor(hasNext, after)
            return Response.success(ApiResponse(cursor = cursor, data = pagedList))
        }
        //scroll to down
        val startIndex = inbox.indexOfFirst { it.id == newAfter }
        if (startIndex == -1) return Response.error(
            404,
            ResponseBody.create(null, "Haven't element after")
        )
        var endIndex = startIndex + pageSize
        if (endIndex > inbox.lastIndex) {
            hasNext = false
            endIndex = inbox.lastIndex
        }
        val pagedList = inbox.subList(startIndex, endIndex)
        after = if (hasNext) pagedList.lastOrNull()?.id else null
        val cursor = Cursor(hasNext, after)
        return Response.success(ApiResponse(cursor = cursor, data = pagedList))
    }

    companion object {
        private val inbox = listOf(
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Alex",
                subject = "Andrey",
                contentPreview = "I hope my test app is not bad=)",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                group = "Others",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface.You could try to better optimize the user interface.You could try to better optimize the user interface.You could try to better optimize the user interface.You could try to better optimize the user interface.You could try to better optimize the user interface.You could try to better optimize the user interface.You could try to better optimize the user interface.You could try to better optimize the user interface.You could try to better optimize the user interface",
                group = "Others",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Others",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Alex",
                subject = "Andrey",
                contentPreview = "I hope my test app is not bad=)",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Notification",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Notification",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Notification",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Notification",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
            MessageDto(
                id = UUID.randomUUID().toString(),
                date = Date(),
                from = "Andrey",
                subject = "Alex",
                contentPreview = "You could try to better optimize the user interface",
                group = "Personal",
                content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                isRead = false,
                isDeleted = false
            ),
        )
    }

    data class ApiResponse(
        val cursor: Cursor,
        val data: List<MessageDto>?,
        val error: Exception? = null
    )
}