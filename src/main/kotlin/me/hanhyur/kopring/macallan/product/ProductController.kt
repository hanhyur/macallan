package me.hanhyur.kopring.macallan.product

import jakarta.validation.Valid
import me.hanhyur.kopring.macallan.common.PagedResponse
import me.hanhyur.kopring.macallan.product.request.ProductRegisterRequest
import me.hanhyur.kopring.macallan.product.request.ProductSearchRequest
import me.hanhyur.kopring.macallan.product.request.ProductUpdateRequest
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
        @RequestBody request: ProductRegisterRequest
    ): ResponseEntity<ProductResponse> {
        val response = productService.register(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // TODO : 상품 벌크 등록 API
    @PostMapping("/bulk")
    fun registerBulkProducts(
        @RequestBody @Valid requests: List<ProductRegisterRequest>
    ): ResponseEntity<List<ProductResponse>> {
        if (requests.isEmpty()) {
            throw IllegalArgumentException("전달된 목록이 비었습니다. request : $requests")
        }

        val response = productService.registerBulk(requests)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long
    ): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(productService.get(id))
    }

    // TODO : 검색 기능 추가
    @PostMapping("/search")
    fun search(
        @RequestBody request: ProductSearchRequest
    ): ResponseEntity<PagedResponse<ProductResponse>> {
        return ResponseEntity.ok(productService.getList(request.pageSize, request.pageNumber))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: ProductUpdateRequest
    ): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(productService.update(id, request))
    }

    @PutMapping("/{id}/options")
    fun updateOptions(
        @PathVariable id: Long,
        @RequestBody request: ProductUpdateRequest
    ): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(productService.update(id, request))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ): ResponseEntity<ProductDeleteResponse> {
        return ResponseEntity.ok(productService.delete(id))
    }

}