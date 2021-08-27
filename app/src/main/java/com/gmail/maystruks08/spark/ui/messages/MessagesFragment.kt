package com.gmail.maystruks08.spark.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.maystruks08.spark.App
import com.gmail.maystruks08.spark.MainActivity
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.databinding.MessageFragmentBinding
import com.gmail.maystruks08.spark.ui.base.BaseFragment
import com.gmail.maystruks08.spark.ui.spark_adapter.SparkAdapter
import com.gmail.maystruks08.spark.ui.spark_adapter.base.MessageSwipeActionHelper
import com.gmail.maystruks08.spark.ui.utils.injectViewModel
import com.gmail.maystruks08.spark.ui.utils.observeInLifecycle
import com.gmail.maystruks08.spark.ui.utils.toolbar.FragmentToolbar
import com.gmail.maystruks08.spark.ui.utils.view_models.BottomView
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class MessagesFragment : BaseFragment(),
    MessageItemController.Interaction,
    BottomItemController.Interaction {

    @Inject
    lateinit var viewModel: MessagesViewModel
    private lateinit var messageAdapter: SparkAdapter
    private var binding: MessageFragmentBinding? = null
    private var snackBar: Snackbar? = null


    private val normalToolbar = FragmentToolbar.Builder()
        .withMenu(R.menu.menu_search_with_settings)
        .withSwitch { viewModel.changeInboxMode(it) }
        .withMenuItems(
            listOf(R.id.action_search),
            listOf(MenuItem.OnMenuItemClickListener { true })
        )
        .withTitle(R.string.toolbar_inbox)
        .build()

    private val groupToolbar = FragmentToolbar.Builder()
        .withNavigationIcon(R.drawable.ic_arrow_back) { requireActivity().onBackPressed() }
        .withTitle(R.string.toolbar_inbox)
        .build()

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
        configCustomBackPressDispatcher()
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

    override fun builder() = FragmentToolbar.Builder().build()

    override fun bindViewModel() {
        viewModel = injectViewModel(viewModeFactory)
        with(viewModel) {
            lifecycleScope.launchWhenStarted {
                messages.collect(::renderViewState)
            }
            lifecycleScope.launchWhenResumed {
                navigation
                    .onEach(::renderNavigationState)
                    .observeInLifecycle(this@MessagesFragment)
            }
            lifecycleScope.launchWhenStarted {
                fragmentToolbar
                    .onEach(::renderToolbarState)
                    .observeInLifecycle(this@MessagesFragment)
            }
        }
    }

    override fun initViews() {
        setupAdapter()
        viewModel.loadMoreData()
    }

    /**
     * Adapter interactions
     */
    override fun onClicked(item: MessageView) {
        viewModel.onMessageItemClicked(item)
    }

    override fun onReadMessageClicked(item: MessageView) {
        viewModel.onReadMessageItemClicked(item)
    }

    override fun onUnreadMessageClicked(item: MessageView) {
        viewModel.onUnreadMessageItemClicked(item)
    }

    override fun onDeleteMessageClicked(item: MessageView) {
        viewModel.onDeleteMessageClicked(item)
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

    private fun renderNavigationState(state: NavigationState) {
        when (state) {
            NavigationState.Nothing -> Unit
            is NavigationState.OpenDetailScreen -> {
                val action = MessagesFragmentDirections.actionMessagesFragmentToDetailFragment(
                    state.item.id,
                    state.item.from
                )
                findNavController(this).navigate(action)
            }
            NavigationState.Exit -> activity?.finishAndRemoveTask()
        }
    }

    private fun renderToolbarState(state: ToolbarMode) {
        when (state) {
            is ToolbarMode.WithBackButton -> (activity as MainActivity).configToolbar(groupToolbar)
            else -> (activity as MainActivity).configToolbar(normalToolbar)
        }
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

            val layoutManager = rvMessages.layoutManager
            rvMessages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val totalItemCount = layoutManager?.itemCount ?: 0
                        var firstVisibleItemPosition = 0
                        if (layoutManager is LinearLayoutManager) firstVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                        if (firstVisibleItemPosition + 8 >= totalItemCount) viewModel.loadMoreData()
                    }
                }
            })
        }
    }

    private fun setUpItemTouchHelper() {
        val messageSwipeActionHelper = object : MessageSwipeActionHelper(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val swipedMessage = messageAdapter.currentList[position]
                if (swipedMessage !is MessageView) return
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

    private fun configCustomBackPressDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            })
    }
}