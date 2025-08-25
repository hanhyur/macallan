package me.hanhyur.kopring.macallan.product.repository

import me.hanhyur.kopring.macallan.product.entity.ProductDetail
import org.springframework.data.jpa.repository.JpaRepository

interface ProductDetailRepository : JpaRepository<ProductDetail, Long> {
}