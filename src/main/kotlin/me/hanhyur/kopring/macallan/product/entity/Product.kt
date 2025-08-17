package me.hanhyur.kopring.macallan.product.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import me.hanhyur.kopring.macallan.common.entity.BaseEntity

@Entity
@Table(name = "product")
class Product (
    @Column(name = "product_name")
    var name: String,

    @OneToOne(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var productOption: ProductOption?,

    @Column(name = "product_category")
    var category: String,

    @OneToOne(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var productDetail: ProductDetail?,

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    var status: Status
) : BaseEntity()  {

    enum class Status(private val label: String) {
        ON_SALE("판매중"),
        SOLD_OUT("품절")
    }

}