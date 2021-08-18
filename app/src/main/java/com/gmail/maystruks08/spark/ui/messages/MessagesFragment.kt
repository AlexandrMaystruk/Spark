package com.gmail.maystruks08.spark.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gmail.maystruks08.spark.App
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.databinding.MessageFragmentBinding
import com.gmail.maystruks08.spark.ui.base.BaseFragment
import com.gmail.maystruks08.spark.ui.spark_adapter.SparkAdapter
import com.gmail.maystruks08.spark.ui.spark_adapter.base.MessageSwipeActionHelper
import com.gmail.maystruks08.spark.ui.utils.injectViewModel
import com.gmail.maystruks08.spark.ui.utils.toolbar.FragmentToolbar
import com.gmail.maystruks08.spark.ui.utils.view_models.BottomView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MessagesFragment : BaseFragment(),
    MessageItemController.Interaction,
    BottomItemController.Interaction {

    @Inject
    lateinit var viewModel: MessagesViewModel
    private lateinit var messageAdapter: SparkAdapter
    private var binding: MessageFragmentBinding? = null
    private var snackBar: Snackbar? = null


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
                messages.collect(::renderViewState)
            }
        }
    }

    override fun initViews() {
        setupAdapter()
        viewModel.provideMessageList()
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
        snackBar?.dismiss()
        binding = null
    }

    override fun clearDependency() {
        App.clearMessageListComponent()
    }


    private fun renderViewState(state: MessageState) {
        binding?.run {
            when (state) {
                MessageState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is MessageState.ShowInboxList -> {
                    progressBar.visibility = View.GONE
                    messageAdapter.submitList(state.data)
                }
                is MessageState.Error -> {
                    progressBar.visibility = View.GONE
                    snackBar = Snackbar.make(root, state.message, Snackbar.LENGTH_LONG).also { it.show() }
                }
            }
        }
    }

    private fun setupAdapter() {
        binding?.run {
            rvMessages.adapter = messageAdapter
            setUpItemTouchHelper()
        }
    }

    private fun setUpItemTouchHelper() {
        val messageSwipeActionHelper = object : MessageSwipeActionHelper(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val swipedMessage = messageAdapter.currentList[position]
                swipedMessage ?: return
                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.onMessageSwipedLeft(swipedMessage)
                } else if (direction == ItemTouchHelper.RIGHT) {
                    viewModel.onMessageSwipedRight(swipedMessage)
                }
                messageAdapter.notifyItemChanged(position)
            }
        }
        ItemTouchHelper(messageSwipeActionHelper).attachToRecyclerView(binding?.rvMessages)
    }

}