package me.hanhyur.kopring.macallan.product

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.hanhyur.kopring.macallan.common.exception.ProductNotFoundException
import me.hanhyur.kopring.macallan.product.entity.Product
import me.hanhyur.kopring.macallan.product.entity.ProductDetail
import me.hanhyur.kopring.macallan.product.entity.ProductOption
import me.hanhyur.kopring.macallan.product.repository.ProductRepository
import me.hanhyur.kopring.macallan.product.request.ProductRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.Optional

class ProductServiceTest {

    private val productRepository: ProductRepository = mockk(relaxed = true)
    private val productService: ProductService = ProductServiceImpl(productRepository)

    private fun createProduct(
        id: Long,
        name: String,
        price: Int,
        quantity: Int,
        category: String,
        description: String
    ): Product {
        val product = Product(
            name = name,
            productOption = null,
            category = category,
            productDetail = null,
        ).apply {
            this.id = id
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }

        val option = ProductOption(
            product = product,
            price = price,
            quantity = quantity,
            discount = 0
        ).apply { this.id = id }

        val detail = ProductDetail(
            product = product,
            description = description
        ).apply { this.id = id }

        product.productOption = option
        product.productDetail = detail

        return product
    }

    @Nested
    @DisplayName("상품 등록")
    inner class Register {

        @Test
        @DisplayName("단일 상품 등록 시, repository save 호출 후 응답 반환")
        fun `register single product`() {
            // given
            val request = ProductRequest("맥켈란 12년", 150000, 10, 0, "위스키", "싱글 몰트 위스키")

            every { productRepository.save(any()) } answers {
                val savedProduct = firstArg<Product>()
                savedProduct.id = 1L
                savedProduct
            }

            // when
            val response = productService.registerProduct(request)

            // then
            assertNotNull(response)
            assertEquals(1L, response.id)
            assertEquals(request.name, response.name)
            verify(exactly = 1) { productRepository.save(any<Product>()) }
        }

        @Test
        @DisplayName("대량 상품 등록 시, 개별 save 호출 후 리스트 반환")
        fun `register bulk products - success`() {
            // given
            val requests = listOf(
                ProductRequest("글렌피딕 18년", 200000, 5, 0, "위스키", "부드러운 맛"),
                ProductRequest("발베니 14년", 180000, 7, 0, "위스키", "달콤한 맛")
            )

            every { productRepository.findByName(any()) } returns false

            every { productRepository.save(any()) } answers {
                val p = firstArg<Product>()
                if (p.name == "글렌피딕 18년") p.id = 1L
                if (p.name == "발베니 14년") p.id = 2L
                p
            }

            // when
            val responses = productService.registerBulkProducts(requests)

            // then
            assertEquals(2, responses.size)
            assertEquals("글렌피딕 18년", responses[0].name)
            assertEquals("발베니 14년", responses[1].name)
            verify(exactly = 2) { productRepository.findByName(any()) }
            verify(exactly = 2) { productRepository.save(any()) }
        }

        @Test
        @DisplayName("대량 상품 등록 시, 목록이 비어있으면 IllegalArgumentException")
        fun `register bulk products - empty list`() {
            // given
            val requests = emptyList<ProductRequest>()

            // when & then
            assertThrows<IllegalArgumentException> {
                productService.registerBulkProducts(requests)
            }
            verify(exactly = 0) { productRepository.save(any()) }
        }

        @Test
        @DisplayName("대량 상품 등록 시, 중복된 이름이 있으면 DuplicateProductNameException")
        fun `register bulk products - duplicate name`() {
            // given
            val requests = listOf(
                ProductRequest("이미 있는 상품", 200000, 5, 0, "위스키", "부드러운 맛")
            )

            every { productRepository.findByName("이미 있는 상품") } returns true

            // when & then
            assertThrows<Exception> {
                productService.registerBulkProducts(requests)
            }

            verify(exactly = 1) { productRepository.findByName("이미 있는 상품") }
            verify(exactly = 0) { productRepository.save(any()) }
        }

    }

    @Nested
    @DisplayName("상품 조회")
    inner class Find() {

        @Test
        @DisplayName("ID로 상품 조회 시, repository findById를 호출 후 상품 반환")
        fun `get product by id - success`() {
            // given
            val productId = 1L
            val foundProduct = createProduct(productId, "맥켈란 12년", 150000, 10, "위스키", "싱글 몰트 위스키")

            every { productRepository.findById(productId) } returns Optional.of(foundProduct)

            // when
            val response = productService.getProduct(productId)

            // then
            assertEquals(foundProduct.id, response.id)
            assertEquals(foundProduct.name, response.name)
            verify(exactly = 1) { productRepository.findById(productId) }
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시, ProductNotFoundException")
        fun `get product by id - not found`() {
            // given
            val productId = 999L
            every { productRepository.findById(productId) } returns Optional.empty()

            // when & then
            assertThrows<ProductNotFoundException> {
                productService.getProduct(productId)
            }
            verify(exactly = 1) { productRepository.findById(productId) }
        }

        @Test
        @DisplayName("상품 목록 조회 시, repository findAll을 호출 후 페이징 반환")
        fun `get product list`() {
            // given
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)
            val products = listOf(
                createProduct(1L, "글렌피딕 18년", 200000, 5, "위스키", "부드러운 맛"),
                createProduct(2L, "발베니 14년", 180000, 7, "위스키", "달콤한 맛")
            )
            val productPage = PageImpl(products, pageable, products.size.toLong())

            every { productRepository.findAll(pageable) } returns productPage

            // when
            val pagedResponse = productService.getProductList(page, size)

            // then
            assertEquals(products.size, pagedResponse.content.size)
            assertEquals("글렌피딕 18년", pagedResponse.content[0].name)
            verify(exactly = 1) { productRepository.findAll(pageable) }
        }

    }

    @Nested
    @DisplayName("상품 수정")
    inner class Update() {

        @Test
        @DisplayName("상품 수정 시, 요청된 내용으로 상품 정보가 변경")
        fun `update product`() {
            // given
            val productId = 1L
            val originalName = "맥켈란 12년"
            val updatedName = "맥켈란 18년"

            val productInDb = createProduct(productId, originalName, 150000, 10, "위스키", "설명")
            val request = ProductRequest(updatedName, 200000, 5, 0, "싱글몰트", "업데이트된 설명")

            every { productRepository.findById(productId) } returns Optional.of(productInDb)

            // when
            val response = productService.updateProduct(productId, request)

            // then
            assertEquals(productId, response.id)
            assertEquals(updatedName, response.name)

            verify(exactly = 1) { productRepository.findById(productId) }
        }

    }
}