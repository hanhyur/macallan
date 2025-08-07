package me.hanhyur.kopring.macallan.product.response

data class ProductDeleteResponse(
    val id: Long
) {

    companion object {
        fun from(id: Long): ProductDeleteResponse {
            return ProductDeleteResponse(
                id = id
            )
        }
    }

}
