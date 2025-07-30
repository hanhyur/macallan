package me.hanhyur.kopring.macallan.repository

import me.hanhyur.kopring.macallan.domain.product.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
}