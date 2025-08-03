package me.hanhyur.kopring.macallan.service

import me.hanhyur.kopring.macallan.common.ApiResponse
import me.hanhyur.kopring.macallan.domain.product.Product
import me.hanhyur.kopring.macallan.domain.product.Status
import me.hanhyur.kopring.macallan.dto.request.ProductRequest
import me.hanhyur.kopring.macallan.dto.response.ProductResponse
import me.hanhyur.kopring.macallan.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductService(
    val productRepository: ProductRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun registerProduct(request: ProductRequest): ApiResponse {
        val product = Product(
            name = request.name,
            description = request.description,
            price = request.price,
            quantity = request.quantity,
            category = request.category,
            status = Status.valueOf(request.status),
        )

        val saved = productRepository.save(product)

        return ApiResponse.ok(ProductResponse.from(saved))
    }

    fun registerBulkProducts(requests: List<ProductRequest>): ApiResponse {
        if (requests.isEmpty()) {
            return ApiResponse.error("상품 목록이 비었습니다")
        }

        val products = requests.mapNotNull { request ->
            try {
                Product(
                    name = request.name,
                    price = request.price,
                    quantity = request.quantity,
                    category = request.category,
                    description = request.description,
                    status = Status.valueOf(request.status.uppercase())
                )
            } catch (e: IllegalArgumentException) {
                logger.warn("잘못된 상품 입력: ${request.name}, 에러: ${e.message}")

                null
            }
        }

        val saved = productRepository.saveAll(products)
        val response = saved.map { ProductResponse.from(it) }

        return ApiResponse.ok(response)
    }

    fun getProduct(id: Long): ApiResponse {
        val product = productRepository.findById(id)
            .orElse(null) ?: return ApiResponse.error("상품을 찾을 수 없습니다")

        return ApiResponse.ok(ProductResponse.from(product))
    }

    fun getProductList(page: Int, size: Int): ApiResponse {
        val pageable: Pageable = PageRequest.of(page, size)
        val products: Page<Product> = productRepository.findAll(pageable)

        val response = products.map { ProductResponse.from(it) }

        return ApiResponse.ok(response)
    }

}