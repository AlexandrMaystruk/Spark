package com.gmail.maystruks08.spark.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gmail.maystruks08.spark.App
import com.gmail.maystruks08.spark.R
import com.gmail.maystruks08.spark.databinding.DetailFragmentBinding
import com.gmail.maystruks08.spark.ui.base.BaseFragment
import com.gmail.maystruks08.spark.ui.utils.injectViewModel
import com.gmail.maystruks08.spark.ui.utils.toolbar.FragmentToolbar
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DetailFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: DetailViewModel

    private var binding: DetailFragmentBinding? = null
    private val args: DetailFragmentArgs by navArgs()

    override fun injectDependency() {
        App.detailComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DetailFragmentBinding
            .inflate(layoutInflater)
            .also { binding = it }
            .root
    }

    override fun builder() = FragmentToolbar
        .Builder()
        .withNavigationIcon(R.drawable.ic_pin) { findNavController(this).navigateUp() }
        .withTitle(args.messageId)
        .build()

    override fun bindViewModel() {
        viewModel = injectViewModel(viewModeFactory)
        with(viewModel) {
            lifecycleScope.launchWhenStarted {
                selectedMessage.collect {
                    binding?.message?.text = it?.content
                }
            }
        }
    }

    override fun initViews() {
        viewModel.requireMessage(args.messageId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun clearDependency() {
        App.clearDetailComponent()
    }
}