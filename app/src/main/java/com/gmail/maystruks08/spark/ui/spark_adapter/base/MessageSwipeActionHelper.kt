package com.gmail.maystruks08.spark.ui.spark_adapter.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.ui.messages.MessageViewHolder

abstract class MessageSwipeActionHelper(
    context: Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_delete)
    private val deleteBg: Drawable

    private val markAsReadIcon = ContextCompat.getDrawable(context, R.drawable.ic_mark_email_read)
    private val markAsReadBg: Drawable

    private var prevDX = -1f

    init {
        deleteBg = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(context, R.color.design_dark_default_color_error))
        }
        markAsReadBg = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(context, R.color.teal_200))
        }
    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder !is MessageViewHolder) return 0
        return super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        when {
            //Swipe left
            dX < 0 -> {
                deleteIcon?.let {
                    val iconTop = itemView.top + (itemHeight - it.intrinsicHeight) / 2
                    val iconMargin = (itemHeight - it.intrinsicHeight) / 3
                    val iconBottom = iconTop + it.intrinsicHeight
                    deleteBg.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    val iconRight = itemView.right - iconMargin
                    val iconLeft = itemView.right - it.intrinsicWidth - iconMargin
                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    deleteBg.draw(c)
                    it.draw(c)
                    prevDX = dX
                }
            }
            //Swipe right
            dX > 0 -> {
                markAsReadIcon?.let {
                    val iconTop = itemView.top + (itemHeight - it.intrinsicHeight) / 2
                    val iconMargin = (itemHeight - it.intrinsicHeight) / 3
                    val iconBottom = iconTop + it.intrinsicHeight
                    markAsReadBg.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.right + dX.toInt(),
                        itemView.bottom
                    )
                    val iconLeft = itemView.left + iconMargin
                    val iconRight = itemView.left + it.intrinsicWidth + iconMargin
                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    markAsReadBg.draw(c)
                    it.draw(c)
                    prevDX = dX
                }
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}