package me.hanhyur.kopring.macallan.domain.product

import jakarta.persistence.*
import me.hanhyur.kopring.macallan.domain.BaseEntity

@Entity
@Table(name = "product")
class Product (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,

    @Column(name = "product_name")
    var name: String,

    @Column(name = "product_price")
    var price: Int,

    @Column(name = "product_description")
    var category: String,

    @Column(name = "product_quantity")
    var description: String,

    var status: Status,
) : BaseEntity()  {
}