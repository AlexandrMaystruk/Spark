package com.gmail.maystruks08.spark.ui.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.databinding.ItemMessageBinding
import com.gmail.maystruks08.spark.ui.spark_adapter.base.BaseViewHolder
import com.gmail.maystruks08.spark.ui.spark_adapter.base.Item
import com.gmail.maystruks08.spark.ui.spark_adapter.base.ItemSparkAdapter
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView

class MessageItemController(
    private val interaction: Interaction
) : ItemSparkAdapter<ItemMessageBinding, MessageView> {

    override fun isRelativeItem(item: Item) = item is MessageView

    override fun getLayoutId() = R.layout.item_message

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemMessageBinding, MessageView> {
        val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding, interaction)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<MessageView>() {

        override fun areItemsTheSame(oldItem: MessageView, newItem: MessageView): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MessageView, newItem: MessageView): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: MessageView, newItem: MessageView): Any? {
            val payloads = MessagePayloads(isMessageStatusChanged = true)
            return if (payloads.isAnyChanges()) payloads
            else super.getChangePayload(oldItem, newItem)
        }
    }

    interface Interaction {
        fun onClicked(item: MessageView)
        fun onLongClicked(item: MessageView)
    }
}

class MessageViewHolder(
    binding: ItemMessageBinding,
    private val interaction: MessageItemController.Interaction,
) : BaseViewHolder<ItemMessageBinding, MessageView>(binding) {

    init {
        binding.root.setOnClickListener {
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
            interaction.onClicked(item)
        }

        binding.root.setOnLongClickListener {
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnLongClickListener false
            interaction.onLongClicked(item)
            return@setOnLongClickListener true
        }
    }

    override fun onBind(item: MessageView) {
        super.onBind(item)
        with(binding) {
            tvMessageFrom.text = item.subject
        }
    }

    override fun onBind(item: MessageView, payloads: List<Any>) {
        super.onBind(item, payloads)
        with(binding) {
            payloads.forEach {
                if (it is MessagePayloads) {
                    if (it.isMessageStatusChanged) renderStatus(item)
                }
            }
        }
    }

    private fun ItemMessageBinding.renderStatus(item: MessageView) {

    }

}


data class MessagePayloads(val isMessageStatusChanged: Boolean) {

    fun isAnyChanges() = isMessageStatusChanged

}