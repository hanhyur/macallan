package me.hanhyur.kopring.macallan.product.mapper

import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.response.ProductResponse
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ProductMapper {
    fun toResponse(product: Product): ProductResponse
}