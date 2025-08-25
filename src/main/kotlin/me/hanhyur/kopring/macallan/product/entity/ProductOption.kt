package me.hanhyur.kopring.macallan.product.entity

import jakarta.persistence.Entity
import me.hanhyur.kopring.macallan.common.entity.BaseEntity

@Entity
class ProductOption(
    var productId: Long,
    var price: Int,
    var quantity: Int,
    var discount: Int,
): BaseEntity()