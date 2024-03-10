package vk.tech.task.domain.repo

import vk.tech.task.domain.model.ProductModel

interface NetworkRepo {

    suspend fun getAllProductsPaging(): List<ProductModel>

    suspend fun getNewPage(skipValue: Long, query: String? = null): List<ProductModel>

    suspend fun getProductById(productId: Long): ProductModel

    suspend fun getProductsPagingBySearchQuery(query: String): List<ProductModel>
}