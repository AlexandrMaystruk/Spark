@file:Suppress("unused")
package com.gmail.maystruks08.spark.ui.utils.toolbar

import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes

class FragmentToolbar(
    @StringRes val title: Int,
    val titleString: String?,
    @MenuRes val menuId: Int,
    @DrawableRes val navigationIcon: Int,
    val navigationIconClickListener: (() -> Unit)?,
    val searchViewTextChangeListener: ((String) -> Unit)?,
    val switchChangeListener: ((Boolean) -> Unit)?,
    val menuItems: MutableList<Int>,
    val menuClicks: MutableList<MenuItem.OnMenuItemClickListener?>
) {
    class Builder {
        private var menuId: Int = NO_TOOLBAR
        private var title: Int = NO_TOOLBAR
        private var titleString: String? = null
        private var navigationIcon: Int = NO_TOOLBAR
        private var navigationIconClickListener: (() -> Unit)? = null
        private var menuItems: MutableList<Int> = mutableListOf()
        private var menuClicks: MutableList<MenuItem.OnMenuItemClickListener?> = mutableListOf()
        private var searchViewTextChangeListener: ((String) -> Unit)? = null
        private var switchChangeListener: ((Boolean) -> Unit)? = null

        fun withTitle(title: Int) = apply { this.title = title }

        fun withTitle(title: String) = apply { this.titleString = title }

        fun withNavigationIcon(
            @DrawableRes navigationIconId: Int,
            navigationIconClickListener: () -> Unit
        ) =
            apply {
                this.navigationIcon = navigationIconId
                this.navigationIconClickListener = navigationIconClickListener
            }

        fun withMenu(@MenuRes menuId: Int) = apply { this.menuId = menuId }

        fun withMenuSearch(searchViewTextChangeListener: (String) -> Unit) = apply {
            this.searchViewTextChangeListener = searchViewTextChangeListener
        }

        fun withSwitch(switchChangeListener: (Boolean) -> Unit) = apply {
            this.switchChangeListener = switchChangeListener
        }

        fun withMenuItems(menuItems: List<Int>, menuClicks: List<MenuItem.OnMenuItemClickListener?>) = apply {
            this.menuItems.addAll(menuItems)
            this.menuClicks.addAll(menuClicks)
        }

        fun build() = FragmentToolbar(
            title,
            titleString,
            menuId,
            navigationIcon,
            navigationIconClickListener,
            searchViewTextChangeListener,
            switchChangeListener,
            menuItems,
            menuClicks
        )
    }

    companion object {
        const val NO_TOOLBAR = -1
    }
}