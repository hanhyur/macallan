package me.hanhyur.kopring.macallan.domain.product

enum class Status(private val status: String) {
    ON_SALE("판매중"),
    SOLD_OUT("품절")
}
