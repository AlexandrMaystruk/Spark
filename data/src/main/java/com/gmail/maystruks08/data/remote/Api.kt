package com.gmail.maystruks08.data.remote

import android.util.Log
import com.gmail.maystruks08.data.pojo.GroupedMessagesDto
import com.gmail.maystruks08.data.pojo.MessageDto
import com.gmail.maystruks08.domain.entity.Cursor
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.mail.Folder.READ_ONLY
import javax.mail.MessagingException
import javax.mail.NoSuchProviderException
import javax.mail.Session
import kotlin.random.Random


class InboxApi @Inject constructor() {

    fun getInbox(user: String, password: String): List<MessageDto> {
        try {
            val host = "imap.gmail.com"
            val prop = Properties()
            prop["mail.imap.host"] = host
            prop["mail.imap.port"] = "993"
            prop["mail.imap.ssl.enable"] = "true"
            val emailSession = Session.getInstance(prop)
//            val emailSession = Session.getInstance(prop,
//                object : Authenticator() {
//                    override fun getPasswordAuthentication(): PasswordAuthentication {
//                        return PasswordAuthentication(user, password)
//                    }
//                })

            emailSession.debug = true

            val store = emailSession.getStore("imap")
//            store.connect(host, user, password)
            store.connect(host, user, password)

            val emailFolder = store.getFolder("INBOX")
            emailFolder.open(READ_ONLY)

            val messages = emailFolder.getMessages(0, 10)

            val messagesDto = messages.map {
                MessageDto(
                    id = it.receivedDate.time.toString(),
                    date = it.sentDate,
                    from = it.from.toString(),
                    header = it.from.toString(),
                    subject = it.subject,
                    contentPreview = it.description,
                    group = it.folder.name,
                    content = it.content.toString(),
                    isRead = false,  //hardcode
                    isDeleted = false //hardcode
                )
            }

            emailFolder.close(true)
            store.close()
            return messagesDto
        } catch (e: NoSuchProviderException) {
            Log.e("GMAIL", e.stackTraceToString())
        } catch (e: MessagingException) {
            Log.e("GMAIL", e.stackTraceToString())
        } catch (e: Exception) {
            Log.e("GMAIL", e.stackTraceToString())
        }
        return emptyList()
    }

//    @GET("/inboxList.json")
//    fun getInboxMessageList(
//        @Query("apiKey") apiKey: String? = null,
//        @Query("page") page: Int? = null,
//        @Query("pageSize") pageSize: Int? = null,
//        @Query("q") source: String? = null
//    ): Response<List<MessageDto>>


    fun mutateMessage(messageDto: MessageDto) {
        val index = inbox.indexOfFirst { it.id == messageDto.id }
        if (index == -1) {
            inbox.add(messageDto)
            inbox.sortedByDescending { it.date }
            return
        }
        inbox[index] = messageDto
        inbox.sortedByDescending { it.date }
    }

    fun getInboxMessagesGroupedMock(
        newAfter: String? = null,
        pageSize: Int
    ): Response<ApiResponse<List<GroupedMessagesDto>>> {
        val result = mutableListOf<GroupedMessagesDto>().apply {
            val sortedList = inbox.apply { sortedByDescending { it.date } }
            sortedList.groupBy { it.group }.forEach { (group, messages) ->
                add(GroupedMessagesDto(group = group, messages.take(5), messages.count()))
            }
        }
        return Response.success(ApiResponse(cursor = Cursor(false, ""), data = result))
    }

    fun getInboxMessagesMock(
        newAfter: String? = null,
        pageSize: Int,
        query: String? = null
    ): Response<ApiResponse<List<MessageDto>>> {

//        getInbox("100for24@gmail.com", "100for24_Run")

        val source = if (query != null) inbox.filter { it.group.contains(query) }
            .sortedBy { it.date } else inbox.sortedByDescending { it.date }
        var hasNext = true
        val after: String?
        //init
        if (newAfter == null) {
            val lastIndex = if (pageSize > source.lastIndex) {
                hasNext = false
                source.lastIndex
            } else {
                hasNext = true
                pageSize
            }
            val pagedList = source.subList(0, lastIndex)
            after = if (hasNext) pagedList.lastOrNull()?.id else null
            val cursor = Cursor(hasNext, after)
            return Response.success(ApiResponse(cursor = cursor, data = pagedList))
        }
        //scroll to down
        val startIndex = source.indexOfFirst { it.id == newAfter }
        if (startIndex == -1) return Response.success(
            ApiResponse(
                cursor = Cursor(false, null),
                data = emptyList()
            )
        )
        var endIndex = startIndex + pageSize
        if (endIndex > source.lastIndex) {
            hasNext = false
            endIndex = source.lastIndex
        }
        val pagedList = source.subList(startIndex, endIndex)
        after = if (hasNext) pagedList.lastOrNull()?.id else null
        val cursor = Cursor(hasNext, after)
        return Response.success(ApiResponse(cursor = cursor, data = pagedList))
    }

    companion object {

        private val random = Random(4)
        private val headersData = listOf(
            "Contact with me",
            "Write me back",
            "I haven't idea whats wrong",
            "Simple header"
        )
        private val fromData = listOf("Alex", "Andrey", "Stepan", "Vovan")
        private val subjectData = listOf("Stas", "Ivan", "Den", "Andrey")
        private val groupsData = listOf("Others", "Personal", "Notification", "Not important")
        private val inbox = mutableListOf<MessageDto>().apply {
            val startDate = Date().time
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
                val header = headersData[randomIndex2]
                val subject = subjectData[groupIndex]
                val group = groupsData[groupIndex]
                val message = MessageDto(
                    id = it.toString(),
                    date = Date(startDate - it * 100 + it),
                    from = from,
                    subject = subject,
                    header = header,
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

    data class ApiResponse<T>(
        val cursor: Cursor,
        val data: T?,
        val error: Exception? = null
    )
}