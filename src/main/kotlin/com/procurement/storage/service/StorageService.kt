package com.procurement.storage.service

import com.procurement.storage.dao.FileDao
import com.procurement.storage.exception.ErrorType
import com.procurement.storage.exception.ExternalException
import com.procurement.storage.model.dto.UploadDataRs
import com.procurement.storage.model.dto.UploadRs
import com.procurement.storage.model.entity.FileEntity
import com.procurement.storage.utils.toLocal
import liquibase.util.file.FilenameUtils
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths


@Service
class StorageService(private val fileDao: FileDao) {

    @Value("\${upload.file.path}")
    private var uploadFilePath: String = ""

    @Value("\${upload.file.folder}")
    private var uploadFileFolder: String = ""

    @Value("\${upload.file.extensions}")
    private var fileExtensions: Array<String> = arrayOf()

    @Value("\${upload.file.max-weight}")
    private var maxFileWeight: Int = 0


    fun uploadFile(fileId: String, file: MultipartFile): UploadRs {
        val fileEntity = fileDao.getOneById(fileId)
        if (fileEntity != null) {
            checkFileName(fileEntity, file)
            checkFileSize(fileEntity, file)
            checkFileHash(fileEntity, file)
            fileEntity.fileOnServer = writeFileToDisk(fileEntity, file)
            fileDao.save(fileEntity)
            return UploadRs(data = UploadDataRs(
                    id = fileEntity.id,
                    url = uploadFilePath + fileEntity.id,
                    dateModified = fileEntity.dateModified?.toLocal(),
                    datePublished = fileEntity.datePublished?.toLocal())
            )
        } else
            throw ExternalException(ErrorType.FILE_NOT_FOUND)
    }


    private fun checkFileWeight(fileWeight: Long) {
        if (fileWeight == 0L || maxFileWeight < fileWeight)
            throw ExternalException(ErrorType.INVALID_SIZE, maxFileWeight.toString())
    }

    private fun checkFileNameAndExtension(fileName: String) {
//      checkFileNameByRegex(fileName)
        val fileExtension: String = FilenameUtils.getExtension(fileName)
        if (fileExtension !in fileExtensions)
            throw ExternalException(ErrorType.INVALID_EXTENSION, fileExtensions.toString())
    }

    private fun checkFileHash(fileEntity: FileEntity, file: MultipartFile) {
        try {
            val uploadFileHash = DigestUtils.md5DigestAsHex(file.inputStream).toUpperCase()
            if (uploadFileHash != fileEntity.hash) {
                throw ExternalException(ErrorType.INVALID_HASH)
            }
        } catch (e: IOException) {
            throw ExternalException(ErrorType.READ_EXCEPTION)
        }
    }

    private fun checkFileName(fileEntity: FileEntity, file: MultipartFile) {
        val fileName = file.originalFilename!!
        if (fileName != fileEntity.fileName) throw ExternalException(ErrorType.INVALID_NAME, fileName)
//      checkFileNameByRegex(fileName)
    }

    private fun checkFileSize(fileEntity: FileEntity, file: MultipartFile) {
        val fileSizeMb = file.size
        if (fileSizeMb > fileEntity.weight) throw ExternalException(ErrorType.INVALID_SIZE)
    }

    private fun writeFileToDisk(fileEntity: FileEntity, file: MultipartFile): String {
        try {
            val fileName = file.originalFilename!!
            if (file.isEmpty) throw ExternalException(ErrorType.EMPTY_FILE, fileName)
            val fileID = fileEntity.id
            val dir = uploadFileFolder + "/" + fileID.substring(0, 2) + "/" + fileID.substring(2, 4) + "/"
            Files.createDirectories(Paths.get(dir))
            val url = dir + fileID
            val targetFile = File(url)
            FileUtils.copyInputStreamToFile(file.inputStream, targetFile)
            return url
        } catch (e: IOException) {
            throw ExternalException(ErrorType.WRITE_EXCEPTION, e.message!!)
        }

    }

//    fun checkFileNameByRegex(fileName: String) {
//        val regex =  Regex(pattern = "[\\[\\]\\\\~!@#$^&*()`;<>?,{}‘“]")
//        val regex =  Regex(pattern = "[;]")
//        val baseName = FilenameUtils.getBaseName(fileName)
//        if (regex.containsMatchIn(baseName)) throw ExternalException(ErrorType.INVALID_NAME, fileName)
//    }

    fun getFileStream(fileOnServer: String): InputStream {
        return Files.newInputStream(Paths.get(fileOnServer))
    }

    fun getFileEntityById(fileId: String): FileEntity {
        val fileEntity = fileDao.getOneById(fileId)
        if (fileEntity != null)
            return if (fileEntity.isOpen) {
                if (fileEntity.fileOnServer == null) {
                    throw ExternalException(ErrorType.NO_FILE_ON_SERVER, fileId)
                }
                fileEntity
            } else {
                throw ExternalException(ErrorType.FILE_IS_CLOSED, fileId)
            }
        else
            throw ExternalException(ErrorType.FILE_NOT_FOUND, fileId)
    }
}
