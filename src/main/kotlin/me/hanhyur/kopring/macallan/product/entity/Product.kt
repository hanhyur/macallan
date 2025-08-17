package me.hanhyur.kopring.macallan.product.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import me.hanhyur.kopring.macallan.common.entity.BaseEntity

@Entity
@Table(name = "product")
class Product (
    @Column(name = "product_name")
    var name: String,

    @Column(name = "product_price")
    var price: Int,

    @Column(name = "product_quantity")
    var quantity: Int,

    @Column(name = "product_category")
    var category: String,

    @Column(name = "product_description")
    var description: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    var status: Status
) : BaseEntity()  {

    enum class Status(private val status: String) {
        ON_SALE("판매중"),
        SOLD_OUT("품절")
    }

}