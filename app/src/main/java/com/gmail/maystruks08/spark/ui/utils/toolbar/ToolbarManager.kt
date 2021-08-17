package com.gmail.maystruks08.spark.ui.utils.toolbar

import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.gmail.maystruks08.spark.R

class ToolbarManager constructor(private var fragmentToolbar: Toolbar) {


    fun prepareToolbar(builder: FragmentToolbar) {
        if (builder.title != -1 || builder.titleString != null) {
            builder.titleString?.run {
                fragmentToolbar.title = this
            }?: kotlin.run {
                fragmentToolbar.setTitle(builder.title)
            }
            fragmentToolbar.setTitleTextColor(ContextCompat.getColor(fragmentToolbar.context, R.color.white))
        } else fragmentToolbar.title = null

        if (builder.navigationIcon != -1) {
            fragmentToolbar.setNavigationIcon(builder.navigationIcon)
            builder.navigationIconClickListener.let { listener ->
                fragmentToolbar.setNavigationOnClickListener {
                    listener?.invoke()
                }
            }
        } else fragmentToolbar.navigationIcon = null

        if (builder.menuId != -1) {
            fragmentToolbar.inflateMenu(builder.menuId)
        } else fragmentToolbar.menu.clear()

        if (builder.searchViewTextChangeListener != null) {
            val menu = fragmentToolbar.menu
            val searchView = menu.findItem(R.id.action_search).actionView as? SearchView
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    builder.searchViewTextChangeListener.invoke(newText)
                    return false
                }
            })
        }

        if (builder.menuItems.isNotEmpty() && builder.menuClicks.isNotEmpty()) {
            val menu = fragmentToolbar.menu
            for ((index, menuItemId) in builder.menuItems.withIndex()) {
                menu?.findItem(menuItemId)
                    ?.setOnMenuItemClickListener(builder.menuClicks[index])
            }
        }
    }
}