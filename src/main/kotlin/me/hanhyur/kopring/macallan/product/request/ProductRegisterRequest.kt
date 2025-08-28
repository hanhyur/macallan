package me.hanhyur.kopring.macallan.product.request

import jakarta.validation.constraints.NotBlank

data class ProductRegisterRequest(
    @field:NotBlank
    val name: String,
    val category: String,
    val price: Int,
    val origin: String,
    val description: String? = null,
    val optionName: String,
    val optionPrice: Int,
    val quantity: Int,
)