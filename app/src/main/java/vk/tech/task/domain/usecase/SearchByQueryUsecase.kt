package vk.tech.task.domain.usecase

import vk.tech.task.domain.model.ProductModel
import vk.tech.task.domain.repo.NetworkRepo
import javax.inject.Inject

class SearchByQueryUsecase @Inject constructor() {

    suspend operator fun invoke(
        repo: NetworkRepo,
        selectedCategory: String?,
        query: String,
    ): List<ProductModel> {
        val data = if (query.isEmpty().not())
            repo.getProductsPagingBySearchQuery(query = query.lowercase())
        else
            repo.getAllProductsPaging()

        return if (selectedCategory != null) {
            data.filter { it.category == selectedCategory }
        } else {
            data
        }
    }
}