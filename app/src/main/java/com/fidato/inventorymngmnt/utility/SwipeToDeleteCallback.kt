package com.fidato.inventorymngmnt.utility

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.ui.master.adapter.CategoryAdapter


class SwipeToDeleteCallback(context: Context, adapter: CategoryAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val mAdapter: CategoryAdapter
    private val icon: Drawable?
    private val background: ColorDrawable
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // used for up and down movements
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
//        mAdapter.deleteTask(position)
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView: View = viewHolder.itemView
        val backgroundCornerOffset =
            20 //so background is behind the rounded corners of itemView
        val iconMargin: Int = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop: Int =
            itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        if (dX > 0) { // Swiping to the right
            val iconLeft: Int = itemView.left + iconMargin + icon.intrinsicWidth
            val iconRight: Int = itemView.left + iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight: Int = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)
        icon.draw(c)
    }

    init {
        mAdapter = adapter
        icon = ContextCompat.getDrawable(context, R.drawable.btn_minus)
        background = ColorDrawable(Color.RED)
    }
}