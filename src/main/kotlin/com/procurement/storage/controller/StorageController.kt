package com.procurement.storage.controller

import com.procurement.storage.exception.ExternalException
import com.procurement.storage.model.dto.ResponseDto
import com.procurement.storage.model.dto.UploadRs
import com.procurement.storage.model.dto.getExceptionResponseDto
import com.procurement.storage.model.dto.getExternalExceptionResponseDto
import com.procurement.storage.service.StorageService
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping(value = ["/storage"])
class StorageController(private val storageService: StorageService) {

    @PostMapping(value = ["/upload/{fileId}"], consumes = ["multipart/form-data"])
    fun uploadFile(@PathVariable(value = "fileId") fileId: String,
                   @RequestParam(value = "file") file: MultipartFile): ResponseEntity<UploadRs> {

        return ResponseEntity(storageService.uploadFile(fileId, file), HttpStatus.CREATED)
    }

    @GetMapping(value = ["/get/{fileId}"])
    fun getFileStream(@PathVariable(value = "fileId") fileId: String, response: HttpServletResponse) {

        val fileEntity = storageService.getFileEntityById(fileId)

        val fileInputStream = storageService.getFileStream(fileEntity.fileOnServer!!)
        response.addHeader("content-disposition", "attachment; filename=\"" + fileEntity.fileName + "\"")
        response.contentType = "application/octet-stream"
        response.status = HttpStatus.OK.value()
        IOUtils.copyLarge(fileInputStream, response.outputStream)
        response.flushBuffer()
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun exception(ex: Exception): ResponseDto {
        return getExceptionResponseDto(ex)
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExternalException::class)
    fun externalException(ex: ExternalException): ResponseDto {
        return getExternalExceptionResponseDto(ex)
    }

}
