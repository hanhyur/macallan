package me.hanhyur.kopring.macallan.product.response

import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.entity.ProductDetail
import me.hanhyur.kopring.macallan.product.entity.ProductOption

data class ProductResponse(
    val id: Long,
    val name: String,
    val option: ProductOption?,
    val category: String,
    val detail: ProductDetail?,
) {

    companion object {
        fun from(product: Product): ProductResponse {
            return ProductResponse(
                id = product.id ?: 0L,
                name = product.name,
                option = product.productOption,
                category = product.category,
                detail = product.productDetail,
            )
        }
    }

}