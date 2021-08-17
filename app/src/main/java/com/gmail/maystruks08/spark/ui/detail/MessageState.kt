package com.gmail.maystruks08.spark.ui.detail

import com.gmail.maystruks08.spark.ui.utils.view_models.MessageDetailView

sealed class DetailViewState {
    object Loading : DetailViewState()
    class ShowMessage(val detail: MessageDetailView) : DetailViewState()
    class Error(val message: String) : DetailViewState()
}