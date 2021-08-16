package com.gmail.maystruks08.spark.ui.spark_adapter.base

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewPaginator(
    private val layoutManager: RecyclerView.LayoutManager?,
    private val threshold: Int
) : RecyclerView.OnScrollListener() {

    abstract val isLastPage: Boolean

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val totalItemCount = layoutManager?.itemCount ?: 0
            var firstVisibleItemPosition = 0
            if (layoutManager is LinearLayoutManager) {
                firstVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }
            if (layoutManager is GridLayoutManager) {
                firstVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }
            if (isLastPage) return
            if (firstVisibleItemPosition + threshold >= totalItemCount) loadMore()
        }
    }

    abstract fun loadMore()
}