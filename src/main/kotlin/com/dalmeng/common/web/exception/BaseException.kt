package com.dalmeng.common.web.exception

abstract class BaseException(
    val statusCode: Int,
    message: String
) : RuntimeException(message)
