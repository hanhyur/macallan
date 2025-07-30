package me.hanhyur.kopring.macallan.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import me.hanhyur.kopring.macallan.domain.product.Product
import org.springframework.stereotype.Service

@Service
class ProductService {

    private val objectMapper = ObjectMapper().registerModule(kotlinModule())

    fun parseProductFromJson(jsonString: String): Product {
        return objectMapper.readValue(jsonString, Product::class.java)
    }
}