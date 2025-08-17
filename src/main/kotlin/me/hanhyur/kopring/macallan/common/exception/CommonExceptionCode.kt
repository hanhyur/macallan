package me.hanhyur.kopring.macallan.common.exception

import org.springframework.http.HttpStatus

enum class CommonExceptionCode(
    val status: HttpStatus,
    val errorCode: String
) {
    WRONG_PRODUCT_INFO(HttpStatus.BAD_REQUEST, "잘못된 상품 정보입니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    DUPLICATE_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 상품명입니다.")
}
