package me.hanhyur.kopring.macallan.common.exception

class DuplicateProductNameException(
    val exceptionCode: CommonExceptionCode
): RuntimeException() {
}
