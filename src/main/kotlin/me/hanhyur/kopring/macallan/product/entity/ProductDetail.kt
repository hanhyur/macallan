package me.hanhyur.kopring.macallan.product.entity

import jakarta.persistence.Entity
import me.hanhyur.kopring.macallan.common.entity.BaseEntity

@Entity
class ProductDetail(
    var productId: Long,
    var description: String,
): BaseEntity()