package me.hanhyur.kopring.macallan.product

import io.github.oshai.kotlinlogging.KotlinLogging
import me.hanhyur.kopring.macallan.common.PagedResponse
import me.hanhyur.kopring.macallan.common.exception.CommonExceptionCode
import me.hanhyur.kopring.macallan.common.exception.ProductNotFoundException
import me.hanhyur.kopring.macallan.common.exception.ServerProcessException
import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.repository.ProductRepository
import me.hanhyur.kopring.macallan.product.request.ProductRequest
import me.hanhyur.kopring.macallan.product.response.ProductDeleteResponse
import me.hanhyur.kopring.macallan.product.response.ProductResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductServiceImpl(
    val productRepository: ProductRepository
): ProductService {
    private val logger = KotlinLogging.logger {}

    @Transactional
    override fun registerProduct(request: ProductRequest): ProductResponse {
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
    override fun registerBulkProducts(requests: List<ProductRequest>): List<ProductResponse> {
        if (requests.isEmpty()) {
            throw IllegalArgumentException("전달된 목록이 비었습니다. request : $requests")
        }

        val products = requests.map { request ->
            Product(
                name = request.name,
                price = request.price,
                quantity = request.quantity,
                category = request.category,
                description = request.description,
                status = Product.Status.valueOf(request.status.uppercase())
            )
        }

        if (products.size != requests.size) {
            throw ServerProcessException(CommonExceptionCode.WRONG_PRODUCT_INFO)
        }

        val saved = productRepository.saveAll(products)

        return saved.map { ProductResponse.from(it) }
    }

    override fun getProduct(id: Long): ProductResponse = ProductResponse.from(this.getProductFromDb(id))

    override fun getProductList(page: Int, size: Int): PagedResponse<ProductResponse> {
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
    override fun updateProduct(id: Long, request: ProductRequest): ProductResponse {
        val productEntityFromDb = this.getProductFromDb(id);

        productEntityFromDb.apply {
            name = this.name
            price = this.price
            quantity = this.quantity
            category = this.category
            description = this.description
            status = this.status
        }

        logger.info {
            """
            id = $id
            name = ${productEntityFromDb.name}
            price = ${productEntityFromDb.price}
            quantity = ${productEntityFromDb.quantity}
            category = ${productEntityFromDb.category}
            description = ${productEntityFromDb.description}
            status = ${productEntityFromDb.status}
        """.trimIndent()
        }

        return ProductResponse.from(productEntityFromDb)
    }

    override fun deleteProduct(id: Long): ProductDeleteResponse {
        val productEntityFromDb = this.getProductFromDb(id);

        productRepository.delete(productEntityFromDb)

        logger.info { "상품 삭제 : id = $id" }

        return ProductDeleteResponse.from(id)
    }

    private fun getProductFromDb(id: Long): Product = this.productRepository.findById(id)
        .orElseThrow { ProductNotFoundException(exceptionCode = CommonExceptionCode.PRODUCT_NOT_FOUND) }

}