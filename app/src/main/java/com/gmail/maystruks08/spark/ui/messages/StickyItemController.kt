package com.gmail.maystruks08.spark.ui.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.databinding.ItemStickyBinding
import com.gmail.maystruks08.spark.ui.spark_adapter.base.BaseViewHolder
import com.gmail.maystruks08.spark.ui.spark_adapter.base.Item
import com.gmail.maystruks08.spark.ui.spark_adapter.base.ItemSparkAdapter
import com.gmail.maystruks08.spark.ui.utils.view_models.StickyView

class StickyItemController : ItemSparkAdapter<ItemStickyBinding, StickyView> {

    override fun isRelativeItem(item: Item) = item is StickyView

    override fun getLayoutId() = R.layout.item_sticky

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemStickyBinding, StickyView> {
        val binding = ItemStickyBinding.inflate(layoutInflater, parent, false)
        return StickyViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<StickyView>() {
        override fun areItemsTheSame(oldItem: StickyView, newItem: StickyView): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: StickyView, newItem: StickyView): Boolean =
            oldItem == newItem
    }

}

class StickyViewHolder(binding: ItemStickyBinding) :
    BaseViewHolder<ItemStickyBinding, StickyView>(binding) {

    override fun onBind(item: StickyView) {
        super.onBind(item)
        with(binding) {
            tvStickyMessageTitle.text = item.title
            tvStickyMessageTitle.setCompoundDrawablesWithIntrinsicBounds(item.icon, 0, 0, 0)
        }
    }
}