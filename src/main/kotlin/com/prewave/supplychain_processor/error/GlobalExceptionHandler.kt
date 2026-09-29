package com.prewave.supplychain_processor.error

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException) = problemDetail(HttpStatus.BAD_REQUEST, e.message)

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException) = problemDetail(HttpStatus.NOT_FOUND, e.message)

    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(e: ConflictException) = problemDetail(HttpStatus.CONFLICT, e.message)

    private fun problemDetail(status: HttpStatus, message: String?) =
        ProblemDetail.forStatusAndDetail(status, message)
}