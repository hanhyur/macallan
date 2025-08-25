package me.hanhyur.kopring.macallan.product.repository

import me.hanhyur.kopring.macallan.product.entity.ProductOption
import org.springframework.data.jpa.repository.JpaRepository

interface ProductOptionRepository : JpaRepository<ProductOption, Long> {
}