package me.hanhyur.kopring.macallan.product

import me.hanhyur.kopring.macallan.common.PagedResponse
import me.hanhyur.kopring.macallan.product.request.ProductRegisterRequest
import me.hanhyur.kopring.macallan.product.request.ProductUpdateRequest
import me.hanhyur.kopring.macallan.product.response.ProductDeleteResponse
import me.hanhyur.kopring.macallan.product.response.ProductResponse

interface ProductService {
    fun register(request: ProductRegisterRequest) : ProductResponse
    fun registerBulk(requests: List<ProductRegisterRequest>): List<ProductResponse>

    fun get(id: Long): ProductResponse
    fun getList(page: Int, size: Int): PagedResponse<ProductResponse>

    fun update(id: Long, request: ProductUpdateRequest) : ProductResponse

    fun delete(id: Long) : ProductDeleteResponse
}