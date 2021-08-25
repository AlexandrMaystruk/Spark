package com.gmail.maystruks08.spark.ui.utils.view_models

import androidx.annotation.DrawableRes
import com.gmail.maystruks08.spark.ui.spark_adapter.base.Item

interface InboxView : Item
data class MessageView(val id: String, val date: String, val subject: String, val isRead: Boolean) : InboxView
data class StickyView(@DrawableRes val icon: Int, val title: String) : InboxView
data class BottomView(val title: String, val group: String) : InboxView