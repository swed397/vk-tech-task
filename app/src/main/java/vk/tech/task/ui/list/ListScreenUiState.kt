package vk.tech.task.ui.list

sealed interface ListScreenUiState {
    val loadingNewPage: Boolean
        get() = false
//    val query: String?
//        get() = null

    data object Loading : ListScreenUiState
    data class Content(
        val items: List<ProductPreviewUiModel>,
        override val loadingNewPage: Boolean = false,
//        override val query: String? = null
    ) : ListScreenUiState

    data object Error : ListScreenUiState
}