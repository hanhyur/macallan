package me.hanhyur.kopring.macallan.dto.response

import me.hanhyur.kopring.macallan.domain.product.Product

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val quantity: Int,
    val category: String,
    val description: String,
    val status: String
) {

    companion object {
        fun from(product: Product): ProductResponse {
            return ProductResponse(
                id = product.id ?: 0L,
                name = product.name,
                price = product.price,
                quantity = product.quantity,
                category = product.category,
                description = product.description,
                status = product.status.name
            )
        }
    }

}