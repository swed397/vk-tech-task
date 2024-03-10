package vk.tech.task.data.network

import vk.tech.task.domain.mapper.toModel
import vk.tech.task.domain.model.ProductModel
import vk.tech.task.domain.repo.NetworkRepo
import javax.inject.Inject

class NetworkRepositoryImp @Inject constructor(private val api: ProductApi) : NetworkRepo {

    override suspend fun getAllProductsPaging(): List<ProductModel> =
        api.getProductsPaging().products.map { it.toModel() }

    override suspend fun getNewPage(skipValue: Long, query: String?): List<ProductModel> =
        if (query.isNullOrEmpty()) {
            api.getProductsPaging(skipValue = skipValue).products.map { it.toModel() }
        } else {
            api.getProductsPagingWithQuery(
                query = query,
                skipValue = skipValue
            ).products.map { it.toModel() }
        }

    override suspend fun getProductById(productId: Long): ProductModel =
        api.getProductsPaging(skipValue = if (productId != 0L) productId - 1 else productId).products
            .first { it.id == productId }
            .toModel()

    override suspend fun getProductsPagingBySearchQuery(query: String): List<ProductModel> =
        api.getProductsPagingWithQuery(query = query).products.map { it.toModel() }
}