package me.hanhyur.kopring.macallan.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.hanhyur.kopring.macallan.domain.product.Product
import me.hanhyur.kopring.macallan.domain.product.Status
import me.hanhyur.kopring.macallan.dto.request.ProductRequest
import me.hanhyur.kopring.macallan.dto.response.ProductResponse
import me.hanhyur.kopring.macallan.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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

    @Test
    fun `상품 조회`() {

    }

    @Test
    fun `상품 수정`() {

    }

    @Test
    fun `상품 삭제`() {

    }

}