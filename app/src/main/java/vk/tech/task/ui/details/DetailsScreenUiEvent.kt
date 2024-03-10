package vk.tech.task.ui.details

sealed interface DetailsScreenUiEvent {
    data class ShowNewImage(val itemIndex: Int) : DetailsScreenUiEvent
}