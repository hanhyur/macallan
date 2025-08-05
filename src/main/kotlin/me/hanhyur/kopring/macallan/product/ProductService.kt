package me.hanhyur.kopring.macallan.product

import io.github.oshai.kotlinlogging.KotlinLogging
import me.hanhyur.kopring.macallan.common.ApiResponse
import me.hanhyur.kopring.macallan.common.PagedResponse
import me.hanhyur.kopring.macallan.common.exception.CommonExceptionCode
import me.hanhyur.kopring.macallan.common.exception.ProductNotFoundException
import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.request.ProductRequest
import me.hanhyur.kopring.macallan.product.response.ProductResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    val productRepository: ProductRepository
) {
    private val logger = KotlinLogging.logger {}

    @Transactional
    fun registerProduct(request: ProductRequest): ProductResponse {
        val product = Product(
            name = request.name,
            description = request.description,
            price = request.price,
            quantity = request.quantity,
            category = request.category,
            status = Product.Status.valueOf(request.status),
        )

        val saved = productRepository.save(product)

        return ProductResponse.from(saved)
    }

    @Transactional
    fun registerBulkProducts(requests: List<ProductRequest>): ApiResponse {
        if (requests.isEmpty()) {
            return ApiResponse.Companion.error("상품 목록이 비었습니다")
        }

        val products = requests.mapNotNull { request ->
            try {
                Product(
                    name = request.name,
                    price = request.price,
                    quantity = request.quantity,
                    category = request.category,
                    description = request.description,
                    status = Product.Status.valueOf(request.status.uppercase())
                )
            } catch (e: IllegalArgumentException) {
                logger.warn("잘못된 상품 입력: ${request.name}, 에러: ${e.message}")

                null
            }
        }

        val saved = productRepository.saveAll(products)
        val response = saved.map { ProductResponse.Companion.from(it) }

        return ApiResponse.ok(response)
    }

    fun getProduct(id: Long): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { ProductNotFoundException(exceptionCode = CommonExceptionCode.PRODUCT_NOT_FOUND) }

        return ProductResponse.from(product)
    }

    fun getProductList(page: Int, size: Int): PagedResponse<ProductResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val products: Page<Product> = productRepository.findAll(pageable)

        return PagedResponse(
            content = products.map { ProductResponse.Companion.from(it) }.content,
            totalElements = products.totalElements,
            totalPages = products.totalPages,
            number = products.number,
            size = products.size
        )
    }

    @Transactional
    fun updateProduct(id: Long, request: ProductRequest): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow()

        product.apply {
            name = this.name
            price = this.price
            quantity = this.quantity
            category = this.category
            description = this.description
            status = this.status
        }

        logger.info { """
            id = $id
            name = $product.name
            price = $product.price
            quantity = $product.quantity
            category = $product.category
            description = $product.description
            status = $product.status
        """.trimIndent() }

        return ProductResponse.from(product)
    }

    fun deleteProduct(id: Long): ApiResponse {
        val exists = productRepository.existsById(id)

        if (!exists) return ApiResponse.Companion.error("상품을 찾을 수 없습니다")

        productRepository.deleteById(id)

        logger.info("상품 삭제 : id = $id")

        return ApiResponse.Companion.ok("삭제되었습니다")
    }

}