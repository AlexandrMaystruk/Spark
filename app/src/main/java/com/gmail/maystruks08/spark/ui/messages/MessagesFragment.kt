package com.gmail.maystruks08.spark.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.gmail.maystruks08.spark.App
import com.gmail.maystruks08.spark.databinding.MessageFragmentBinding
import com.gmail.maystruks08.spark.ui.base.BaseFragment
import com.gmail.maystruks08.spark.ui.spark_adapter.SparkAdapter
import com.gmail.maystruks08.spark.ui.utils.injectViewModel
import com.gmail.maystruks08.spark.ui.utils.toolbar.FragmentToolbar
import com.gmail.maystruks08.spark.ui.utils.view_models.MessageView
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MessagesFragment : BaseFragment(), MessageItemController.Interaction {

    @Inject
    lateinit var viewModel: MessagesViewModel
    private lateinit var messageAdapter: SparkAdapter
    private var binding: MessageFragmentBinding? = null


    override fun injectDependency() {
        App.messagesComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messageAdapter = SparkAdapter(listOf(MessageItemController(this)))
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

    override fun builder(): FragmentToolbar {
        return FragmentToolbar.Builder().build()
    }

    override fun bindViewModel() {
        viewModel = injectViewModel(viewModeFactory)
        with(viewModel) {
            lifecycleScope.launchWhenStarted {
                messages.collect {
                    messageAdapter.submitList(it)
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
        viewModel.onMessageItemClicked(item)
    }

    override fun onLongClicked(item: MessageView) {
        viewModel.onMessageItemLongClicked(item)
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