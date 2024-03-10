package vk.tech.task.domain.mapper

import vk.tech.task.data.network.model.ProductInfoRs
import vk.tech.task.domain.model.ProductModel

fun ProductInfoRs.toModel(): ProductModel =
    ProductModel(
        id = id ?: -1,
        title = title ?: "",
        description = description ?: "",
        price = price ?: 0,
        discountPercentage = discountPercentage ?: 0.0,
        rating = rating ?: 0.0,
        brand = brand ?: "",
        category = category ?: "",
        imgUrl = imgUrl ?: "",
        imagesList = imagesList ?: listOf(imgUrl ?: "")
    )