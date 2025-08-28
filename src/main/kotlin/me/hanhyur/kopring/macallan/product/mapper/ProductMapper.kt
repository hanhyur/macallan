package me.hanhyur.kopring.macallan.product.mapper

import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.entity.ProductDetail
import me.hanhyur.kopring.macallan.product.entity.ProductOption
import me.hanhyur.kopring.macallan.product.request.ProductRegisterRequest
import me.hanhyur.kopring.macallan.product.request.ProductUpdateRequest
import me.hanhyur.kopring.macallan.product.response.ProductResponse
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
)
interface ProductMapper {
    fun toProduct(productRegisterRequest: ProductRegisterRequest): Product

    fun toDetail(productRegisterRequest: ProductRegisterRequest): ProductDetail

    fun toOption(productRegisterRequest: ProductRegisterRequest): ProductOption

    fun toUpdateProduct(productUpdateRequest: ProductUpdateRequest, @MappingTarget product: Product): Product

    fun toUpdateDetail(productUpdateRequest: ProductUpdateRequest, @MappingTarget productDetail: ProductDetail): ProductDetail

    // fun toUpdateOptions()

    fun toResponse(product: Product): ProductResponse
}