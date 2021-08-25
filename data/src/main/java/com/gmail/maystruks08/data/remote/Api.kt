package com.gmail.maystruks08.data.remote

import com.gmail.maystruks08.data.pojo.MessageDto
import com.gmail.maystruks08.domain.entity.Cursor
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.random.Random


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
            ResponseBody.create(null, "Haven't elements after")
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

        private val random = Random(4)
        private val fromData = listOf("Alex", "Andrey", "Stepan", "Vovan")
        private val subjectData = listOf("Stat", "Ivan", "Den", "Andrey")
        private val groupsData = listOf("Others", "Personal", "Notification", "Not important")
        private val inbox = mutableListOf<MessageDto>().apply {
            repeat(40) {
                val randomIndex1 = random.nextInt(0, 3)
                val randomIndex2 = random.nextInt(0, 3)
                val groupIndex =  when{
                    it  < 10 -> 0
                    it  < 20 -> 1
                    it  < 30 -> 2
                    else -> 3
                }
                val from = fromData[randomIndex1]
                val subject = subjectData[randomIndex2]
                val group = groupsData[groupIndex]
                val message = MessageDto(
                    id = it.toString(),
                    date = Date(),
                    from = from,
                    subject = subject,
                    contentPreview = "I hope my test app is not bad=)",
                    content = "I hope my test app is not bad=)\nI hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)I hope my test app is not bad=)",
                    group = group,
                    isRead = false,
                    isDeleted = false
                )
                add(message)
            }
        }
    }

    data class ApiResponse(
        val cursor: Cursor,
        val data: List<MessageDto>?,
        val error: Exception? = null
    )
}