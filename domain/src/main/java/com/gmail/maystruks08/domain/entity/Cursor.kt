package com.gmail.maystruks08.domain.entity

data class Cursor(
    val hasNext: Boolean,
    val newAfter: String?
)