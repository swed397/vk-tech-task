package vk.tech.task.domain.usecase

import vk.tech.task.domain.model.ProductModel
import vk.tech.task.domain.repo.NetworkRepo
import vk.tech.task.ui.list.ListScreenUiState
import javax.inject.Inject

class AddNewPageUsecase @Inject constructor() {

    suspend operator fun invoke(
        repo: NetworkRepo,
        currentState: ListScreenUiState.Content
    ): List<ProductModel> {
        val data = if (currentState.query.isEmpty()) {
            repo.getNewPage(skipValue = currentState.items.size.toLong())
        } else {
            repo.getNewPage(
                skipValue = currentState.items.size.toLong(),
                query = currentState.query
            )
        }
        if (currentState.selectedCategory.isNullOrEmpty().not()) {
            data.filter { it.category == currentState.selectedCategory }
        }

        return data
    }
}