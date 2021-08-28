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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DetailFragment : BaseFragment() {

    @Inject
    lateinit var viewModel: DetailViewModel

    private var binding: DetailFragmentBinding? = null
    private var snackBar: Snackbar? = null
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
        .withNavigationIcon(R.drawable.ic_arrow_back) { findNavController(this).navigateUp() }
        .withTitle(args.title)
        .build()

    override fun bindViewModel() {
        viewModel = injectViewModel(viewModeFactory)
        with(viewModel) {
            lifecycleScope.launchWhenStarted {
                selectedMessage.collect(::renderViewState)
            }
            lifecycleScope.launchWhenStarted {
                buttonState.collect(::renderButtonsViewState)
            }
            lifecycleScope.launchWhenStarted {
                backNavigation.collect { findNavController(this@DetailFragment).navigateUp() }
            }
        }
    }

    override fun initViews() {
        viewModel.requireMessage(args.messageId)
        binding?.run {
            tvChangeReadStatus.setOnClickListener {
                viewModel.onChangeReadStatusClicked(args.messageId)
            }
            tvDeleteMessage.setOnClickListener {
                viewModel.onDeleteMessageClicked(args.messageId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackBar?.dismiss()
        binding = null
    }

    override fun clearDependency() {
        App.clearDetailComponent()
    }

    private fun renderViewState(state: DetailViewState){
        binding?.run {
            when(state){
                DetailViewState.Loading -> progressBar.visibility = View.VISIBLE
                is DetailViewState.ShowMessage -> {
                    progressBar.visibility = View.GONE
                    with(state.detail){
                        tvMessageHeader.text = header
                        tvMessageFrom.text = from
                        tvContent.text = content
                        tvDate.text = date
                    }
                }
                is DetailViewState.Error -> {
                    progressBar.visibility = View.GONE
                    snackBar = Snackbar.make(root, state.message, Snackbar.LENGTH_LONG).also { it.show() }
                }
            }
        }
    }

    private fun renderButtonsViewState(state: DetailViewButtonState) {
        binding?.run {
            when (state) {
                DetailViewButtonState.Hide -> {
                    viewButtonBackground.visibility = View.GONE
                    tvChangeReadStatus.visibility = View.GONE
                    tvDeleteMessage.visibility = View.GONE
                }
                is DetailViewButtonState.Show -> {
                    tvChangeReadStatus.text = state.readStatusTitle
                    viewButtonBackground.visibility = View.VISIBLE
                    tvChangeReadStatus.visibility = View.VISIBLE
                    tvDeleteMessage.visibility = View.VISIBLE
                }
            }
        }
    }
}