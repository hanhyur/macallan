package me.hanhyur.kopring.macallan.common.exception

import me.hanhyur.kopring.macallan.common.exception.enumeration.CommonExceptionCodeType

class DuplicateProductNameException(
    val exceptionCode: CommonExceptionCodeType,
    message : String
): RuntimeException(message)
