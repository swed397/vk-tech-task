package vk.tech.task.ui.details

import vk.tech.task.domain.model.ProductModel

sealed interface DetailsScreenUiState {
    data object Loading : DetailsScreenUiState
    data class Content(val item: ProductModel, val showImageItem: Int?) : DetailsScreenUiState
    data object Error : DetailsScreenUiState
}