package com.procurement.storage.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.storage.exception.ExternalException
import org.slf4j.LoggerFactory


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

fun getExternalExceptionResponseDto(error: ExternalException, id: String? = null): ResponseDto {
    LoggerFactory.getLogger(ExternalException::class.java).warn("400.14." + error.code + " : " + error.msg)
    return ResponseDto(
            errors = listOf(ResponseErrorDto(
                    code = "400.14." + error.code,
                    description = error.msg
            )),
            id = id)
}