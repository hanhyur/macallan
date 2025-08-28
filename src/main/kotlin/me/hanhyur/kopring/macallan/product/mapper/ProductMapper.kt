package me.hanhyur.kopring.macallan.product.mapper

import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.entity.ProductDetail
import me.hanhyur.kopring.macallan.product.entity.ProductOption
import me.hanhyur.kopring.macallan.product.request.ProductRegisterRequest
import me.hanhyur.kopring.macallan.product.request.ProductUpdateRequest
import me.hanhyur.kopring.macallan.product.response.ProductResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
)
interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toProduct(productRegisterRequest: ProductRegisterRequest): Product

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toDetail(productRegisterRequest: ProductRegisterRequest): ProductDetail

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toOption(productRegisterRequest: ProductRegisterRequest): ProductOption

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toUpdateEntity(productUpdateRequest: ProductUpdateRequest, @MappingTarget product: Product): Product

    fun toResponse(product: Product): ProductResponse
}