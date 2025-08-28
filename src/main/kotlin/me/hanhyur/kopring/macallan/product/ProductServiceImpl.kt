package me.hanhyur.kopring.macallan.product

import io.github.oshai.kotlinlogging.KotlinLogging
import me.hanhyur.kopring.macallan.common.PagedResponse
import me.hanhyur.kopring.macallan.common.exception.enumeration.CommonExceptionCodeType
import me.hanhyur.kopring.macallan.common.exception.DuplicateProductNameException
import me.hanhyur.kopring.macallan.common.exception.ProductNotFoundException
import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.entity.ProductDetail
import me.hanhyur.kopring.macallan.product.mapper.ProductMapper
import me.hanhyur.kopring.macallan.product.repository.ProductDetailRepository
import me.hanhyur.kopring.macallan.product.repository.ProductOptionRepository
import me.hanhyur.kopring.macallan.product.repository.ProductRepository
import me.hanhyur.kopring.macallan.product.request.ProductRegisterRequest
import me.hanhyur.kopring.macallan.product.request.ProductUpdateRequest
import me.hanhyur.kopring.macallan.product.response.ProductDeleteResponse
import me.hanhyur.kopring.macallan.product.response.ProductResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productOptionRepository: ProductOptionRepository,
    private val productDetailRepository: ProductDetailRepository,
    private val productMapper: ProductMapper
): ProductService {
    private val logger = KotlinLogging.logger {}

    @Transactional
    override fun register(
        request: ProductRegisterRequest
    ): ProductResponse {
        val product = productMapper.toProduct(request)
        val savedProduct = productRepository.save(product)

        val detail = productMapper.toDetail(request)
        detail.productId = savedProduct.id!!
        productDetailRepository.save(detail)

        val option = productMapper.toOption(request)
        option.productId = savedProduct.id!!
        productOptionRepository.save(option)

        return productMapper.toResponse(savedProduct)
    }

    @Transactional
    override fun registerBulk(
        requests: List<ProductRegisterRequest>
    ): List<ProductResponse> {
        val result = requests.mapNotNull { request ->
            try {
                this.checkExistName(request.name)

                val product = productMapper.toProduct(request)
                val savedProduct = productRepository.save(product)

                val detail = productMapper.toDetail(request)
                detail.productId = savedProduct.id!!
                productDetailRepository.save(detail)

                val option = productMapper.toOption(request)
                option.productId = savedProduct.id!!
                productOptionRepository.save(option)

                productMapper.toResponse(savedProduct)
            } catch (e: Exception) {
                logger.error {
                    """
                        상품 등록 중 오류 발생 : ${e.message}
                        요청 내용 : $request
                    """.trimIndent()
                }

                null
            }
        }

        return result
    }

    override fun get(id: Long): ProductResponse {
        val product = getProductFromDb(id)

        return productMapper.toResponse(product)
    }

    override fun getList(
        page: Int,
        size: Int
    ): PagedResponse<ProductResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val products: Page<Product> = productRepository.findAll(pageable)

        return PagedResponse(
            content = products.content.map(productMapper::toResponse),
            totalElements = products.totalElements,
            totalPages = products.totalPages,
            number = products.number,
            size = products.size
        )
    }

    @Transactional
    override fun update(
        id: Long,
        request: ProductUpdateRequest
    ): ProductResponse {
        val productEntityFromDb = this.getProductFromDb(id)
        val productDetailFromDb = this.getProductDetailFromDb(id)

        productMapper.toUpdateProduct(request, productEntityFromDb)
        productMapper.toUpdateDetail(request, productDetailFromDb)

        logger.info {
            """
            id = $id
            name = ${productEntityFromDb.name}
            category = ${productEntityFromDb.category}
        """.trimIndent()
        }

        return productMapper.toResponse(productEntityFromDb)
    }

    override fun delete(
        id: Long
    ): ProductDeleteResponse {
        val productEntityFromDb = this.getProductFromDb(id);

        productRepository.delete(productEntityFromDb)

        logger.info { "상품 삭제 : id = $id" }

        return ProductDeleteResponse(id)
    }

    private fun getProductFromDb(id: Long): Product = productRepository.findById(id)
        .orElseThrow { ProductNotFoundException(
            exceptionCode = CommonExceptionCodeType.PRODUCT_NOT_FOUND,
            message = "해당 상품을 찾을 수 없습니다. id = $id"
        ) }

    private fun getProductDetailFromDb(id: Long): ProductDetail = productDetailRepository.findById(id)
        .orElseThrow { ProductNotFoundException(
            exceptionCode = CommonExceptionCodeType.PRODUCT_NOT_FOUND,
            message = "해당 상품을 찾을 수 없습니다. id = $id"
        ) }

    private fun checkExistName(name: String) {
        if (productRepository.findByName(name)) {
            throw DuplicateProductNameException(
                exceptionCode = CommonExceptionCodeType.DUPLICATE_PRODUCT_NAME,
                message = "이미 존재하는 상품명입니다. name = $name"
            )
        }
    }
}