package com.gmail.maystruks08.domain.entity

data class GroupedMessages(val group: String, val messages: List<Message>, val count: Int)