package me.hanhyur.kopring.macallan.common.exception

class ServerProcessException(
    val exceptionCode : CommonExceptionCode
): RuntimeException() {
}