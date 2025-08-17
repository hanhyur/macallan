package me.hanhyur.kopring.macallan.product.repository

import me.hanhyur.kopring.macallan.product.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
}