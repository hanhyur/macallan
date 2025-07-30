package me.hanhyur.kopring.macallan.service

import me.hanhyur.kopring.macallan.domain.product.Status
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductServiceTest {

    @Test
    fun `json으로부터 상품을 파싱한다`() {
        val jsonString = """{"id": 1, "name": "Test Product", "price": 30000, "category": "test", "description": "Test Description", "status": "ON_SALE"}"""
        val productService = ProductService()
        val product = productService.parseProductFromJson(jsonString)

        assertEquals(1L, product.id)
        assertEquals("Test Product", product.name)
        assertEquals(30000, product.price)
        assertEquals("test", product.category)
        assertEquals("Test Description", product.description)
        assertEquals(Status.ON_SALE, product.status)
    }

}