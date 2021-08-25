package com.gmail.maystruks08.domain

data class Cursor(
    val hasNext: Boolean,
    val newAfter: String?
)

class PagedData<T>(
    val cursor: Cursor,
    val data: T
)