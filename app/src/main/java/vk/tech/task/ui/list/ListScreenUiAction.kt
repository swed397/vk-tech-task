package vk.tech.task.ui.list

sealed interface ListScreenUiAction {
    data class Navigate(val route: String) : ListScreenUiAction
}