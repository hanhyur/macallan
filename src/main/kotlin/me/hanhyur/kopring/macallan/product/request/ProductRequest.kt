package me.hanhyur.kopring.macallan.product.request

import org.jetbrains.annotations.NotNull

data class ProductRequest(
    @field:NotNull
    val name: String,
    val price: Int,
    val quantity: Int,
    val discount: Double,
    val category: String,
    val description: String,
    val status: String
) {
}