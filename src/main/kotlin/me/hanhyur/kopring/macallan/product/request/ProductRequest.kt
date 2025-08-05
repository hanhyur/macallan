package me.hanhyur.kopring.macallan.product.request

data class ProductRequest(
    val name: String,
    val price: Int,
    val quantity: Int,
    val category: String,
    val description: String,
    val status: String
) {
}