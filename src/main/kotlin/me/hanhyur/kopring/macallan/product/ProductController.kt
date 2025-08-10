package me.hanhyur.kopring.macallan.product

import jakarta.validation.Valid
import me.hanhyur.kopring.macallan.common.PagedResponse
import me.hanhyur.kopring.macallan.product.request.ProductRequest
import me.hanhyur.kopring.macallan.product.request.ProductSearchRequest
import me.hanhyur.kopring.macallan.product.response.ProductDeleteResponse
import me.hanhyur.kopring.macallan.product.response.ProductResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/products")
class ProductController(private val productService: ProductService) {

    @PostMapping
    fun register(
        @RequestBody request: ProductRequest
    ): ResponseEntity<ProductResponse> {
        val response = productService.registerProduct(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // TODO : 상품 벌크 등록 API
    @PostMapping("/bulk")
    fun registerBulkProducts(
        @RequestBody @Valid requests: List<ProductRequest>
    ): ResponseEntity<List<ProductResponse>> {
        val response = productService.registerBulkProducts(requests)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long
    ): ResponseEntity<ProductResponse> {
        val response = productService.getProduct(id)

        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    // TODO : 검색 기능 추가
    @PostMapping("/search")
    fun search(
        @RequestBody request: ProductSearchRequest
    ): ResponseEntity<PagedResponse<ProductResponse>> {
        val pagedResponse = productService.getProductList(request.pageSize, request.pageNumber)

        return ResponseEntity.status(HttpStatus.OK).body(pagedResponse)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: ProductRequest
    ): ResponseEntity<ProductResponse> {
        val response = productService.updateProduct(id, request)

        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ): ResponseEntity<ProductDeleteResponse> {
        val response = productService.deleteProduct(id)

        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

}