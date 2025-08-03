package me.hanhyur.kopring.macallan.service

import me.hanhyur.kopring.macallan.common.ApiResponse
import me.hanhyur.kopring.macallan.domain.product.Product
import me.hanhyur.kopring.macallan.domain.product.Status
import me.hanhyur.kopring.macallan.dto.request.ProductRequest
import me.hanhyur.kopring.macallan.dto.response.PagedResponse
import me.hanhyur.kopring.macallan.dto.response.ProductResponse
import me.hanhyur.kopring.macallan.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

        val response = PagedResponse(
            content = products.map { ProductResponse.from(it) }.content,
            totalElements = products.totalElements,
            totalPages = products.totalPages,
            number = products.number,
            size = products.size
        )

        return ApiResponse.ok(response)
    }

    @Transactional
    fun updateProduct(id: Long, request: ProductRequest): ApiResponse {
        val product = productRepository.findById(id)
            .orElse(null) ?: return ApiResponse.error("상품을 찾을 수 없습니다")

        product.name = request.name
        product.price = request.price
        product.quantity = request.quantity
        product.category = request.category
        product.description = request.description
        product.status = Status.valueOf(request.status.uppercase())

        logger.info("상품 수정 : $product")

        return ApiResponse.ok(ProductResponse.from(product))
    }

    fun deleteProduct(id: Long): ApiResponse {
        val exists = productRepository.existsById(id)

        if (!exists) return ApiResponse.error("상품을 찾을 수 없습니다")

        productRepository.deleteById(id)

        logger.info("상품 삭제 : id = $id")

        return ApiResponse.ok("삭제되었습니다")
    }

}