package com.gmail.maystruks08.spark.ui.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.databinding.ItemBottomBinding
import com.gmail.maystruks08.spark.ui.spark_adapter.base.BaseViewHolder
import com.gmail.maystruks08.spark.ui.spark_adapter.base.Item
import com.gmail.maystruks08.spark.ui.spark_adapter.base.ItemSparkAdapter
import com.gmail.maystruks08.spark.ui.utils.view_models.BottomView

class BottomItemController(
    private val interaction: Interaction
) : ItemSparkAdapter<ItemBottomBinding, BottomView> {

    override fun isRelativeItem(item: Item) = item is BottomView

    override fun getLayoutId() = R.layout.item_bottom

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemBottomBinding, BottomView> {
        val binding = ItemBottomBinding.inflate(layoutInflater, parent, false)
        return BottomViewHolder(interaction, binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<BottomView>() {
        override fun areItemsTheSame(oldItem: BottomView, newItem: BottomView): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: BottomView, newItem: BottomView): Boolean =
            oldItem == newItem
    }

    interface Interaction {
        fun onViewAllClicked(item: BottomView)
    }

}

class BottomViewHolder(
    private val interaction: BottomItemController.Interaction,
    binding: ItemBottomBinding
) : BaseViewHolder<ItemBottomBinding, BottomView>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
            interaction.onViewAllClicked(item)
        }
    }

    override fun onBind(item: BottomView) {
        super.onBind(item)
        with(binding) {
            tvStickyMessageTitle.text = item.title
        }
    }

}