package com.gmail.maystruks08.domain.entity

class PagedData<T>(
    val cursor: Cursor,
    val data: T
)