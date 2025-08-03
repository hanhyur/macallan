package me.hanhyur.kopring.macallan.controller

import me.hanhyur.kopring.macallan.common.ApiResponse
import me.hanhyur.kopring.macallan.dto.request.ProductRequest
import me.hanhyur.kopring.macallan.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/product")
class ProductController(private val productService: ProductService) {

    @PostMapping("/register")
    fun registerProduct(@RequestBody request: ProductRequest): ApiResponse {
        return productService.registerProduct(request)
    }

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: Long): ApiResponse {
        return productService.getProduct(id)
    }

    @GetMapping("/list")
    fun getProducts(@RequestParam page: Int, @RequestParam pageSize: Int): ApiResponse {
        return productService.getProductList(page, pageSize)
    }

}