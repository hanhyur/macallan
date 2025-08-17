package me.hanhyur.kopring.macallan.product

import io.github.oshai.kotlinlogging.KotlinLogging
import me.hanhyur.kopring.macallan.common.PagedResponse
import me.hanhyur.kopring.macallan.common.exception.CommonExceptionCode
import me.hanhyur.kopring.macallan.common.exception.DuplicateProductNameException
import me.hanhyur.kopring.macallan.common.exception.ProductNotFoundException
import me.hanhyur.kopring.macallan.common.exception.ServerProcessException
import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.entity.ProductDetail
import me.hanhyur.kopring.macallan.product.entity.ProductOption
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
    override fun registerProduct(
        request: ProductRequest
    ): ProductResponse {
        val product = Product(
            name = request.name,
            productOption = null,
            category = request.category,
            productDetail = null,
            status = Product.Status.valueOf(request.status),
        )

        // 이게 맞나...
        val option = ProductOption(
            product = product,
            price = request.price,
            quantity = request.quantity,
            discount = request.discount,
        )
        product.productOption = option

        val detail = ProductDetail(
            product = product,
            description = request.description,
        )
        product.productDetail = detail

        val saved = productRepository.save(product)

        return ProductResponse.from(saved)
    }

    @Transactional
    override fun registerBulkProducts(
        requests: List<ProductRequest>
    ): List<ProductResponse> {
        if (requests.isEmpty()) {
            throw IllegalArgumentException("전달된 목록이 비었습니다. request : $requests")
        }

        // 이게 맞나...
        val result: MutableList<ProductResponse> = ArrayList()

        requests.forEach { request ->
            try {
                this.checkExistName(request.name)

                val product = Product(
                    name = request.name,
                    productOption = null,
                    category = request.category,
                    productDetail = null,
                    status = Product.Status.valueOf(request.status.uppercase())
                )

                // 중복되는데 이게 맞나...
                val option = ProductOption(
                    product = product,
                    price = request.price,
                    quantity = request.quantity,
                    discount = request.discount,
                )
                product.productOption = option

                val detail = ProductDetail(
                    product = product,
                    description = request.description,
                )
                product.productDetail = detail

                val saved = productRepository.save(product)

                result.add(ProductResponse.from(saved))
            } catch (e : Exception) {
                // 씁 이렇게 던지는게 아닌거 같은데...
                throw ServerProcessException(CommonExceptionCode.WRONG_PRODUCT_INFO)
            }
        }

        // ??? 뭔가 뭔가임...
        return result
    }

    override fun getProduct(id: Long): ProductResponse = ProductResponse.from(this.getProductFromDb(id))

    override fun getProductList(
        page: Int,
        size: Int
    ): PagedResponse<ProductResponse> {
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
    override fun updateProduct(
        id: Long,
        request: ProductRequest
    ): ProductResponse {
        val productEntityFromDb = this.getProductFromDb(id);

        productEntityFromDb.apply {
            name = this.name
            productOption = this.productOption
            category = this.category
            productDetail = this.productDetail
            status = this.status
        }

        // 저게 맞나..
        logger.info {
            """
            id = $id
            name = ${productEntityFromDb.name}
            price = ${productEntityFromDb.productOption?.price}
            quantity = ${productEntityFromDb.productOption?.price}
            category = ${productEntityFromDb.category}
            description = ${productEntityFromDb.productDetail?.description}
            status = ${productEntityFromDb.status}
        """.trimIndent()
        }

        return ProductResponse.from(productEntityFromDb)
    }

    override fun deleteProduct(
        id: Long
    ): ProductDeleteResponse {
        val productEntityFromDb = this.getProductFromDb(id);

        productRepository.delete(productEntityFromDb)

        logger.info { "상품 삭제 : id = $id" }

        return ProductDeleteResponse.from(id)
    }

    private fun getProductFromDb(id: Long): Product = productRepository.findById(id)
        .orElseThrow { ProductNotFoundException(exceptionCode = CommonExceptionCode.PRODUCT_NOT_FOUND) }

    private fun checkExistName(name: String) {
        if (productRepository.findByName(name)) {
            throw DuplicateProductNameException(exceptionCode = CommonExceptionCode.DUPLICATE_PRODUCT_NAME)
        }
    }
}