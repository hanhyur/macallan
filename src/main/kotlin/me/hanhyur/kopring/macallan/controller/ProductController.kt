package me.hanhyur.kopring.macallan.controller

import me.hanhyur.kopring.macallan.common.ApiResponse
import me.hanhyur.kopring.macallan.dto.request.ProductRequest
import me.hanhyur.kopring.macallan.service.ProductService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/products")
class ProductController(private val productService: ProductService) {

    @PostMapping()
    fun registerProduct(@RequestBody request: ProductRequest): ApiResponse {
        return productService.registerProduct(request)
    }

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: Long): ApiResponse {
        return productService.getProduct(id)
    }

    // TODO : 검색 기능 추가
    @PostMapping("/search")
    fun getProducts(@RequestParam page: Int, @RequestParam pageSize: Int): ApiResponse {
        return productService.getProductList(page, pageSize)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody request: ProductRequest): ApiResponse {
        return productService.updateProduct(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ApiResponse {
        return productService.deleteProduct(id)
    }

}