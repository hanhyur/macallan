package me.hanhyur.kopring.macallan.product.entity

import jakarta.persistence.Entity
import me.hanhyur.kopring.macallan.common.entity.BaseEntity

@Entity
class Product (
    var name: String,
    var category: String,
) : BaseEntity()