package com.gmail.maystruks08.spark.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.gmail.maystruks08.spark.App
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.databinding.MessageFragmentBinding
import com.gmail.maystruks08.spark.ui.base.BaseFragment
import com.gmail.maystruks08.spark.ui.spark_adapter.SparkAdapter
import com.gmail.maystruks08.spark.ui.utils.injectViewModel
import com.gmail.maystruks08.spark.ui.utils.toolbar.FragmentToolbar
import com.gmail.maystruks08.spark.ui.utils.view_models.BottomView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MessagesFragment : BaseFragment(),
    MessageItemController.Interaction,
    BottomItemController.Interaction {

    @Inject
    lateinit var viewModel: MessagesViewModel
    private lateinit var messageAdapter: SparkAdapter
    private var binding: MessageFragmentBinding? = null


    override fun injectDependency() {
        App.messagesComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messageAdapter = SparkAdapter(
            listOf(
                StickyItemController(),
                MessageItemController(this),
                BottomItemController(this)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MessageFragmentBinding
            .inflate(layoutInflater)
            .also { binding = it }
            .root
    }

    override fun builder() = FragmentToolbar.Builder()
        .withMenu(R.menu.menu_search_with_settings)
        .withMenuItems(
            listOf(R.id.action_search),
            listOf(MenuItem.OnMenuItemClickListener {
                //  viewModel.onClicked()
                true
            })
        )
        .withTitle(R.string.toolbar_inbox)
        .build()

    override fun bindViewModel() {
        viewModel = injectViewModel(viewModeFactory)
        with(viewModel) {
            lifecycleScope.launchWhenStarted {
                messages.collect {
                    messageAdapter.submitList(it)
                }
                showDetailFragment.collect {

                }
            }
        }
    }

    override fun initViews() {
        binding?.run {
            rvMessages.adapter = messageAdapter
        }
    }

    /**
     * Adapter interactions
     */
    override fun onClicked(item: MessageView) {
        val action = MessagesFragmentDirections.actionMessagesFragmentToDetailFragment(item.id, item.subject)
        findNavController(this).navigate(action)
    }

    override fun onLongClicked(item: MessageView) {
        viewModel.onMessageItemLongClicked(item)
    }

    override fun onViewAllClicked(item: BottomView) {
        viewModel.onShowAllMessageFromGroupClicked(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun clearDependency() {
        App.clearMessageListComponent()
    }


    companion object{

        fun getInstance(): MessagesFragment = MessagesFragment()
    }
}