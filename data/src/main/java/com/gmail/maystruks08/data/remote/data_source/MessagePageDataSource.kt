package com.gmail.maystruks08.data.remote.data_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gmail.maystruks08.data.DataMapper
import com.gmail.maystruks08.data.local.MessagesDAO
import com.gmail.maystruks08.data.remote.InboxApi
import com.gmail.maystruks08.domain.entity.Message
import retrofit2.HttpException
import javax.inject.Inject

class MessagePageDataSource @Inject constructor(
    private val inboxMapper: DataMapper,
    private val inboxService: InboxApi,
    private val messageDAO: MessagesDAO,
) : PagingSource<Int, Message>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        try {
//            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
//            val pageSize = params.loadSize.coerceAtMost(MAX_PAGE_SIZE)
//            val response = inboxService.getInboxMessagesMock(page = pageNumber, pageSize = pageSize)
//
//            if (response.isSuccessful) {
//                val messages = response.body()?.map { inboxMapper.toEntity(it) }.orEmpty()
//                val nextPageNumber = if (messages.isEmpty()) null else pageNumber + 1
//                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
//                return LoadResult.Page(messages, prevPageNumber, nextPageNumber)
//            }
//            return LoadResult.Error(HttpException(response))
            return LoadResult.Error(Exception())
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        val anchorPosition =  state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }


    companion object {
        const val INITIAL_PAGE_NUMBER = 1
        const val MAX_PAGE_SIZE = 10
    }
}