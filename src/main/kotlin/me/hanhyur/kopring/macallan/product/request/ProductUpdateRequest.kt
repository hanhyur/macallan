package me.hanhyur.kopring.macallan.product.request

data class ProductUpdateRequest(
    val name: String? = null,
    val category: String,
    val price: Int,
)