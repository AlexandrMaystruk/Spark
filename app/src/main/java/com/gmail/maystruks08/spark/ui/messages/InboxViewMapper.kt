package com.gmail.maystruks08.spark.ui.messages

import android.content.res.Resources
import androidx.annotation.StringRes
import com.gmail.maystruks08.domain.entity.GroupedMessages
import com.gmail.maystruks08.domain.entity.Message
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.ui.utils.toPrintFormat
import com.gmail.maystruks08.spark.ui.utils.view_models.*
import javax.inject.Inject

class InboxViewMapper @Inject constructor(private val resources: Resources) {

    fun toDetailView(message: Message): MessageDetailView {
        return message.run {
            MessageDetailView(
                id,
                date.toPrintFormat(),
                from,
                subject,
                content,
                isRead,
                isDeleted
            )
        }
    }

    fun toInboxView(data: List<GroupedMessages>): List<InboxView> {
        return mutableListOf<InboxView>().apply {
            data.forEach { (group, messages, count) ->
                if (messages.isEmpty()) return@forEach
                add(StickyView(R.drawable.ic_pin, group))
                addAll(toViews(messages))
                val title = "${getString(R.string.action_show_all)} $count"
                add(
                    BottomView(
                        title = title,
                        group = group
                    )
                )
            }
        }
    }

    fun toInboxView(data: Map<String, List<Message>>): List<InboxView> {
        return mutableListOf<InboxView>().apply {
            data.forEach { (group, messages) ->
                if (messages.isEmpty()) return@forEach
                add(StickyView(R.drawable.ic_pin, group))
                addAll(toViews(messages))
                val title = "${getString(R.string.action_show_all)} ${messages.count()}"
                add(
                    BottomView(
                        title = title,
                        group = group
                    )
                )
            }
        }
    }

    fun toViews(messages: List<Message>): List<MessageView> {
        return messages.map { toView(it) }
    }

    fun toView(message: Message): MessageView {
        return message.run {
            MessageView(id, date.toString(), subject, isRead)
        }
    }

    fun getString(@StringRes id: Int): String {
        return resources.getString(id)
    }
}

