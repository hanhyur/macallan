package me.hanhyur.kopring.macallan.product

import me.hanhyur.kopring.macallan.common.PagedResponse
import me.hanhyur.kopring.macallan.product.request.ProductRequest
import me.hanhyur.kopring.macallan.product.response.ProductDeleteResponse
import me.hanhyur.kopring.macallan.product.response.ProductResponse

interface ProductService {
    fun registerProduct(request: ProductRequest) : ProductResponse
    fun registerBulkProducts(requests: List<ProductRequest>): List<ProductResponse>

    fun getProduct(id: Long): ProductResponse
    fun getProductList(page: Int, size: Int): PagedResponse<ProductResponse>

    fun updateProduct(id: Long, request: ProductRequest) : ProductResponse

    fun deleteProduct(id: Long) : ProductDeleteResponse
}