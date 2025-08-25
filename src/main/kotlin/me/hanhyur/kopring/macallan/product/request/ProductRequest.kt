package me.hanhyur.kopring.macallan.product.request

import jakarta.validation.constraints.NotBlank

data class ProductRequest(
    @field:NotBlank
    val name: String,
    val price: Int,
    val quantity: Int,
    val discount: Int? = 0,
    val category: String,
    val description: String? = null,
)