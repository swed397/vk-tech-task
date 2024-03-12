package vk.tech.task.domain.usecase

import vk.tech.task.domain.model.ProductModel
import vk.tech.task.domain.repo.NetworkRepo
import vk.tech.task.ui.list.ChipsUiModel
import vk.tech.task.ui.list.ListScreenUiState
import javax.inject.Inject

class SelectCategoryUsecase @Inject constructor() {

    suspend operator fun invoke(
        repo: NetworkRepo,
        categoriesChips: List<ChipsUiModel>,
        category: String
    ): Pair<List<ProductModel>, List<ChipsUiModel>> {
        val newChipsList = categoriesChips
            .map { it.copy(selected = if (it.name == category) it.selected.not() else false) }
            .toMutableList()

        val data =
            if (newChipsList.firstOrNull { it.selected } != null)
                repo.getProductsByCategory(category) else repo.getAllProductsPaging()

        return data to newChipsList
    }
}