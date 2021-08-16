package com.gmail.maystruks08.spark.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gmail.maystruks08.spark.di.base.DaggerViewModelFactory
import com.gmail.maystruks08.spark.ui.utils.toolbar.FragmentToolbar
import com.gmail.maystruks08.spark.ui.utils.toolbar.ToolbarManager
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    var toolbarManager: ToolbarManager? = null

    @Inject
    lateinit var viewModeFactory: DaggerViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependency()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarManager = ToolbarManager(builder(), view).apply { prepareToolbar() }
        bindViewModel()
        initViews()
    }

    protected abstract fun injectDependency()

    protected abstract fun builder(): FragmentToolbar

    protected abstract fun bindViewModel(): Unit?

    protected abstract fun initViews()

    protected abstract fun clearDependency()

    override fun onDestroyView() {
        toolbarManager = null
        super.onDestroyView()
    }

    override fun onDetach() {
        clearDependency()
        super.onDetach()
    }
}