package me.hanhyur.kopring.macallan.product.response

import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.entity.ProductDetail
import me.hanhyur.kopring.macallan.product.entity.ProductOption

data class ProductResponse(
    val id: Long,
    val name: String,
    val category: String,
)