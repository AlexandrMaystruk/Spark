package com.gmail.maystruks08.data.remote

import com.gmail.maystruks08.data.pojo.MessageDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
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
        page: Int? = null,
        pageSize: Int? = null
    ): Response<List<MessageDto>> {
        val pageNotNull = if (page == null || page == 0) defPage else page
        val pageSizeNotNull = if (pageSize == null || pageSize == 0) defPageSize else pageSize
        val startIndex = pageNotNull * pageSizeNotNull
        val pagedList = inbox.subList(startIndex, startIndex + pageSizeNotNull)
        return Response.success(pagedList)
    }

    companion object {

        private const val defPageSize = 8
        private const val defPage = 0
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
        )
    }
}