package com.dalmeng.common.web.exception

import com.dalmeng.common.web.response.BaseResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<BaseResponse<Nothing>> {
        return ResponseEntity
            .status(e.statusCode)
            .body(
                BaseResponse.error(
                    statusCode = e.statusCode,
                    message = e.message ?: e.toString()
                )
            )
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: org.springframework.http.HttpHeaders,
        statusCode: org.springframework.http.HttpStatusCode,
        request: org.springframework.web.context.request.WebRequest
    ): ResponseEntity<Any>? {
        val status = HttpStatus.valueOf(statusCode.value())
        log.error("MVC Exception - ${ex::class.simpleName} - ${ex.message}")

        return ResponseEntity
            .status(status)
            .body(
                BaseResponse.error(
                    statusCode = status.value(),
                    message = ex.message ?: "Request error"
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(
        e: Exception,
        request: HttpServletRequest
    ): ResponseEntity<BaseResponse<Nothing>> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        log.error("Unexpected Exception - ${request.method} ${request.requestURI}", e)

        return ResponseEntity
            .status(status)
            .body(
                BaseResponse.error(
                    statusCode = status.value(),
                    message = e.message ?: "Unexpected error"
                )
            )
    }
}
