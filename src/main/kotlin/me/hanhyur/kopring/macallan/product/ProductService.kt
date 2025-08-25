package me.hanhyur.kopring.macallan.product

import me.hanhyur.kopring.macallan.common.PagedResponse
import me.hanhyur.kopring.macallan.product.request.ProductRequest
import me.hanhyur.kopring.macallan.product.response.ProductDeleteResponse
import me.hanhyur.kopring.macallan.product.response.ProductResponse

interface ProductService {
    fun register(request: ProductRequest) : ProductResponse
    fun registerBulk(requests: List<ProductRequest>): List<ProductResponse>

    fun get(id: Long): ProductResponse
    fun getList(page: Int, size: Int): PagedResponse<ProductResponse>

    fun update(id: Long, request: ProductRequest) : ProductResponse

    fun delete(id: Long) : ProductDeleteResponse
}