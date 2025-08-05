package me.hanhyur.kopring.macallan.product

import me.hanhyur.kopring.macallan.common.ApiResponse
import me.hanhyur.kopring.macallan.product.request.ProductRequest
import me.hanhyur.kopring.macallan.product.request.ProductSearchRequest
import me.hanhyur.kopring.macallan.product.response.ProductResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/products")
class ProductController(private val productService: ProductService) {

    @PostMapping()
    fun register(
        @RequestBody request: ProductRequest
    ): ResponseEntity<ProductResponse> {
        val response = productService.registerProduct(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // TODO : 상품 벌크 등록 API
    fun registerBulkProducts() {

    }

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long
    ): ProductResponse {
        return productService.getProduct(id)
    }

    // TODO : 검색 기능 추가
    @PostMapping("/search")
    fun search(
        @RequestBody request: ProductSearchRequest
    ): ApiResponse {
        return productService.getProductList(request.pageSize, request.pageNumber)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: ProductRequest): ApiResponse {
        return productService.updateProduct(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ApiResponse {
        return productService.deleteProduct(id)
    }

}