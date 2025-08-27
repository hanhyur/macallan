package me.hanhyur.kopring.macallan.product.mapper

import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.entity.ProductOption
import me.hanhyur.kopring.macallan.product.request.ProductRegisterRequest
import me.hanhyur.kopring.macallan.product.response.ProductResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    fun toProduct(productRegisterRequest: ProductRegisterRequest): Product

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    fun toProductOption(productRegisterRequest: ProductRegisterRequest): ProductOption

    fun toUpdateEntity()

    fun toResponse(product: Product): ProductResponse
}