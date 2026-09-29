package com.prewave.supplychain_processor.error

open class BadRequestException(message: String) : RuntimeException(message)

open class NotFoundException(message: String) : RuntimeException(message)

open class ConflictException(message: String) : RuntimeException(message)
