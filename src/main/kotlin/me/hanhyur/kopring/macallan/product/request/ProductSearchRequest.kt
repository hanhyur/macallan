package me.hanhyur.kopring.macallan.product.request

data class ProductSearchRequest(
    val pageNumber: Int = 1,
    val pageSize: Int = 10,
)