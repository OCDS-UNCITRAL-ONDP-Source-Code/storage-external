package com.procurement.storage.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.storage.exception.ExternalException


@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseDto(

        val errors: List<ResponseErrorDto>? = null,
        val data: Any? = null,
        val id: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseErrorDto(

        val code: String,
        val description: String?
)


fun getExceptionResponseDto(exception: Exception): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "500.14.00",
                    description = exception.message
            )))
}

fun getExternalExceptionResponseDto(error: ExternalException, id: String? = null): ResponseDto {
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.14." + error.code,
                    description = error.msg
            )),
            id = id)
}