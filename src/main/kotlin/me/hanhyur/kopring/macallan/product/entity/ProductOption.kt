package me.hanhyur.kopring.macallan.product.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import me.hanhyur.kopring.macallan.common.entity.BaseEntity

@Entity
class ProductOption(
    @OneToOne
    @JoinColumn(name = "product_id")
    var product: Product? = null,

    @Column(name = "price")
    var price: Int,

    @Column(name = "quantity")
    var quantity: Int,

    @Column(name = "discount")
    var discount: Int,
): BaseEntity() {
}