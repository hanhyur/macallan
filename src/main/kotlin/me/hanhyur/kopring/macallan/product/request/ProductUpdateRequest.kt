package me.hanhyur.kopring.macallan.product.request

data class ProductUpdateRequest(
    val name: String? = null,
    val category: String? = null,
    val price: Int? = null,
    val origin: String? = null,
    val description: String? = null,
    val optionName: String? = null,
    val optionPrice: Int? = null,
    val quantity: Int? = null,
)