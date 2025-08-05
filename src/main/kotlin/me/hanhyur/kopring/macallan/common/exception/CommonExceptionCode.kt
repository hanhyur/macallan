package me.hanhyur.kopring.macallan.common.exception

import org.springframework.http.HttpStatus

enum class CommonExceptionCode(
    val status: HttpStatus,
    val errorCode: String
) {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
}
