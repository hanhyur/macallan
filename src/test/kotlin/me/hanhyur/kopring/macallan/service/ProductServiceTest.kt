package me.hanhyur.kopring.macallan.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.hanhyur.kopring.macallan.domain.product.Product
import me.hanhyur.kopring.macallan.domain.product.Status
import me.hanhyur.kopring.macallan.dto.request.ProductRequest
import me.hanhyur.kopring.macallan.dto.response.PagedResponse
import me.hanhyur.kopring.macallan.dto.response.ProductResponse
import me.hanhyur.kopring.macallan.repository.ProductRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

class ProductServiceTest {

    private val productRepository: ProductRepository = mockk(relaxed = true)
    private val productService = ProductService(productRepository)

    @Nested
    @DisplayName("상품 등록")
    inner class RegisterProducts {

        @Test
        fun `단일 상품 등록 성공`() {
            // given
            val request = ProductRequest(
                name = "맥켈란 12년",
                price = 1500000,
                quantity = 10,
                category = "위스키",
                description = "싱글 몰트 위스키",
                status = "ON_SALE"
            )

            val savedProduct = Product(
                id = 1L,
                name = request.name,
                price = request.price,
                quantity = request.quantity,
                category = request.category,
                description = request.description,
                status = Status.ON_SALE
            )

            every { productRepository.save(any()) } returns savedProduct

            // when
            val response = productService.registerProduct(request)

            println()

            // then
            assertTrue(response.success)
            assertEquals("맥켈란 12년", (response.data as ProductResponse).name)
            verify(exactly = 1) { productRepository.save(any<Product>()) }
        }

        @Test
        fun `대량 상품 등록 성공`() {
            // given
            val requests = listOf(
                ProductRequest("글렌피딕 18년", 200000, 5, "위스키", "부드러운 맛", "ON_SALE"),
                ProductRequest("발베니 14년", 180000, 7, "위스키", "달콤한 맛", "SOLD_OUT")
            )

            val savedProducts = requests.mapIndexed { i, request ->
                Product(
                    id = i + 1L,
                    name = request.name,
                    price = request.price,
                    quantity = request.quantity,
                    category = request.category,
                    description = request.description,
                    status = Status.valueOf(request.status)
                )
            }

            every { productRepository.saveAll(any<List<Product>>()) } returns savedProducts

            // when
            val response = productService.registerBulkProducts(requests)

            // then
            assertTrue(response.success)
            assertEquals(2, (response.data as? List<*>)?.size)
            verify(exactly = 1) { productRepository.saveAll(any<List<Product>>()) }
        }

        @Test
        fun `대량 등록 실패`() {
            // given
            val requests = emptyList<ProductRequest>()

            // when
            val response = productService.registerBulkProducts(requests)

            // then
            assertFalse(response.success)
            assertEquals("상품 목록이 비었습니다", response.message)
            verify(exactly = 0) { productRepository.saveAll(any<List<Product>>()) }
        }

    }

    @Nested
    @DisplayName("상품 조회")
    inner class FindProducts() {

        @Test
        fun `단일 상품 조회 성공`() {
            // given
            val id = 1L
            val product = Product(
                name = "맥켈란 12년",
                price = 150000,
                quantity = 10,
                category = "위스키",
                description = "싱글 몰트 위스키",
                status = Status.ON_SALE
            )

            every { productRepository.findById(id) } returns Optional.of(product)

            // when
            val response = productService.getProduct(id)

            // then
            assertTrue(response.success)

            val data = response.data as ProductResponse

            assertEquals("맥켈란 12년", data.name)
            assertEquals(150000, data.price)
            verify(exactly = 1) { productRepository.findById(id) }
        }

        @Test
        fun `존재하지 않는 상품 조회 - 실패`() {
            // given
            val id = 999L

            every { productRepository.findById(id) } returns Optional.empty()

            // when
            val response = productService.getProduct(id)

            // then
            assertFalse(response.success)
            assertEquals("상품을 찾을 수 없습니다", response.message)
            assertNull(response.data)
            verify(exactly = 1) { productRepository.findById(id) }
        }

        @Test
        fun `상품 목록 조회 성공`() {
            // given
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)
            val products = listOf(
                Product(1L, "글렌피딕 18년", 150000, 10, "위스키", "부드러운 맛", Status.ON_SALE),
                Product(2L, "발베니 14년", 180000, 5, "위스키", "달콤한 맛", Status.ON_SALE)
            )
            val productPage = PageImpl(products, pageable, products.size.toLong())

            every { productRepository.findAll(pageable) } returns productPage

            // when
            val response = productService.getProductList(page, size)

            // then
            assertTrue(response.success)

            val dataList = (response.data as PagedResponse<*>).content
            assertEquals(2, dataList.size)
            assertEquals("글렌피딕 18년", (dataList[0] as ProductResponse).name)
            assertEquals("발베니 14년", (dataList[1] as ProductResponse).name)
            verify(exactly = 1) { productRepository.findAll(pageable) }
        }

    }

    @Nested
    @DisplayName("상품 수정")
    inner class UpdateProducts() {

        @Test
        fun `상품 수정 - 성공`() {
            // given
            val id = 1L
            val existingProduct = Product(
                id,
                name = "맥켈란 12년",
                price = 150000,
                quantity = 5,
                category = "위스키",
                description = "기존 설명",
                status = Status.ON_SALE
            )

            val request = ProductRequest(
                name = "맥켈란 18년",
                price = 200000,
                quantity = 10,
                category = "싱글몰트",
                description = "업데이트 설명",
                status = "SOLD_OUT"
            )

            every { productRepository.findById(id) } returns Optional.of(existingProduct)

            // when
            val response = productService.updateProduct(id, request)

            // then
            assertTrue(response.success)

            val updated = response.data as ProductResponse
            assertEquals("맥켈란 18년", updated.name)
            assertEquals(200000, updated.price)
            assertEquals(10, updated.quantity)
            assertEquals("싱글몰트", updated.category)
            assertEquals("업데이트 설명", updated.description)
            assertEquals("SOLD_OUT", updated.status)
            verify(exactly = 1) { productRepository.findById(id) }
        }

        @Test
        fun `상품 수정 - 실패(존재하지 않는 상품)`() {
            // given
            val id = 999L
            val request = ProductRequest(
                name = "글랜리벳",
                price = 80000,
                quantity = 3,
                category = "위스키",
                description = "설명",
                status = "ON_SALE",
            )

            every { productRepository.findById(id) } returns Optional.empty()

            // when
            val response = productService.updateProduct(id, request)

            // then
            assertFalse(response.success)
            assertEquals("상품을 찾을 수 없습니다", response.message)
            assertNull(response.data)
            verify(exactly = 1) { productRepository.findById(id) }
        }

    }

}