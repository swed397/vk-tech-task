package vk.tech.task.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import vk.tech.task.data.network.model.ProductsRs

interface ProductApi {

    @GET("products")
    suspend fun getProductsPaging(
        @Query("skip") skipValue: Long = 0,
        @Query("limit") limitValue: Long = 20
    ): ProductsRs

    @GET("products/search")
    suspend fun getProductsPagingWithQuery(
        @Query("skip") skipValue: Long = 0,
        @Query("limit") limitValue: Long = 20,
        @Query("q") query: String,
    ): ProductsRs
}