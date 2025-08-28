package me.hanhyur.kopring.macallan.product.entity

import jakarta.persistence.Entity
import me.hanhyur.kopring.macallan.common.entity.BaseEntity

@Entity
class ProductDetail(
    var productId: Long,
    var price: Int,
    var origin: String,
    var description: String,
): BaseEntity()