package com.gmail.maystruks08.domain

class PagedData<T>(val hasNext: Boolean, val hasPrevious: Boolean, val data: T)