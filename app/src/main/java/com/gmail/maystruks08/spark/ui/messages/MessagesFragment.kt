package com.gmail.maystruks08.spark.ui.messages

import androidx.lifecycle.lifecycleScope
import com.gmail.maystruks08.spark.App
import com.gmail.maystruks08.spark.ui.base.BaseFragment
import com.gmail.maystruks08.spark.ui.utils.injectViewModel
import com.gmail.maystruks08.spark.ui.utils.toolbar.FragmentToolbar
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MessagesFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: MessagesViewModel


    override fun injectDependency() {
        App.messagesComponent?.inject(this)
    }

    override fun builder(): FragmentToolbar {
        return FragmentToolbar.Builder().build()
    }

    override fun bindViewModel() {
        viewModel = injectViewModel(viewModeFactory)
        with(viewModel) {

            lifecycleScope.launchWhenStarted {
                messages.collect {

                }
            }
        }

    }

    override fun initViews() {

    }

    override fun clearDependency() {
        App.clearMessageListComponent()
    }
}