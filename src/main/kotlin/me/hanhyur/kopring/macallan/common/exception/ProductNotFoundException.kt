package me.hanhyur.kopring.macallan.common.exception

class ProductNotFoundException(
    val exceptionCode : CommonExceptionCode
): RuntimeException() {

}