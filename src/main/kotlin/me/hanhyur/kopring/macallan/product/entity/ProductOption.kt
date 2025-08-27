package me.hanhyur.kopring.macallan.product.entity

import jakarta.persistence.Entity
import me.hanhyur.kopring.macallan.common.entity.BaseEntity

@Entity
class ProductOption(
    var productId: Long,
    var optionName: String,
    var optionPrice: Int,
    var quantity: Int,
): BaseEntity()