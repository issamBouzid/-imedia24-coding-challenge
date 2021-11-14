package de.imedia24.shop.exception

import org.springframework.http.HttpStatus

data class ApiError(
        private val _message: String?,
        val status: HttpStatus = HttpStatus.BAD_REQUEST,
        val code: Int = status.value()
){
    val message: String
        get() = _message ?: "Something went wrong"
}