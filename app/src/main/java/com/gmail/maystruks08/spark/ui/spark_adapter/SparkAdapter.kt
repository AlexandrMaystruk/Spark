package com.gmail.maystruks08.spark.ui.spark_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.gmail.maystruks08.spark.ui.spark_adapter.base.BaseViewHolder
import com.gmail.maystruks08.spark.ui.spark_adapter.base.Item
import com.gmail.maystruks08.spark.ui.spark_adapter.base.ItemSparkAdapter
import com.gmail.maystruks08.spark.ui.spark_adapter.base.SparkAdapterDiffUtil

@Suppress("UNCHECKED_CAST")
class SparkAdapter(
    private val list: List<ItemSparkAdapter<*, *>>,
) : ListAdapter<Item, BaseViewHolder<ViewBinding, Item>>(SparkAdapterDiffUtil(list)) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding, Item> {
        val inflater = LayoutInflater.from(parent.context)
        return list.find { it.getLayoutId() == viewType }
            ?.getViewHolder(inflater, parent)
            ?.let { it as BaseViewHolder<ViewBinding, Item> }
            ?: throw IllegalArgumentException("View type not found: $viewType")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, Item>, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBinding, Item>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        getItem(position)?.let {
            if (payloads.isNotEmpty()) {
                holder.onBind(it, payloads)
                return
            }
            holder.onBind(it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let { item ->
            list.find { it.isRelativeItem(item) }
                ?.getLayoutId()
                ?: throw IllegalArgumentException("View type not found: $item")
        } ?: throw IllegalArgumentException("View type not found for position $position")
    }
}

