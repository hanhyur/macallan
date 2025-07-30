package me.hanhyur.kopring.macallan.domain.product

import jakarta.persistence.*

@Entity
@Table(name = "product")
class Product(
    @Column(name = "product_name")
    var name: String,
    @Column(name = "product_price")
    var price: Int,
    @Column(name = "product_description")
    var category: String,
    @Column(name = "product_quantity")
    var description: String,
    var status: Status,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null
}