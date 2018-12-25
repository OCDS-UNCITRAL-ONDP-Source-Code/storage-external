package com.procurement.storage.controller

import com.procurement.storage.exception.BpeErrorException
import com.procurement.storage.model.dto.bpe.*
import com.procurement.storage.service.StorageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/command")
class CommandController(private val storageService: StorageService) {

    @PostMapping
    fun command(@RequestBody commandMessage: CommandMessage): ResponseEntity<ResponseDto> {
        return ResponseEntity(execute(commandMessage), HttpStatus.OK)
    }

    fun execute(cm: CommandMessage): ResponseDto {
        return when (cm.command) {
            CommandType.VALIDATE -> storageService.validateDocumentsBatch(cm)
            CommandType.PUBLISH -> storageService.setPublishDateBatch(cm)
        }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception): ResponseDto {
        return when (ex) {
            is BpeErrorException -> getErrorExceptionResponseDto(ex)
            else -> getExceptionResponseDto(ex)
        }
    }


}



