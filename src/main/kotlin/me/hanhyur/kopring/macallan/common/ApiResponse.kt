package me.hanhyur.kopring.macallan.common

import org.slf4j.LoggerFactory

data class ApiResponse(
    val success: Boolean,
    val data: Any? = null,
    val message: String? = null
) {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)

        fun ok(data: Any? = null): ApiResponse {
            logger.info("Product Info : ${data.toString()}")

            return ApiResponse(true, data)
        }

        fun error(message: String? = null) = ApiResponse(false, message = message)
    }
}