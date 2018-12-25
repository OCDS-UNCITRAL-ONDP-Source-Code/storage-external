package com.procurement.storage.exception

enum class ErrorType constructor(val code: String, val message: String) {

    FILE_NOT_FOUND("00.01", "File not found: "),
    INVALID_ID("00.02", "Invalid documents ids: "),
    INVALID_SIZE("00.03", "Invalid file size for registration, max: "),
    INVALID_EXTENSION("00.04", "Invalid file extension for registration, valid: "),
    INVALID_HASH("00.05", "Invalid file hash: "),
    READ_EXCEPTION("00.06", "File read exception: "),
    WRITE_EXCEPTION("00.06", "File write exception: "),
    INVALID_NAME("00.07", "Invalid file name: "),
    EMPTY_FILE("00.08", "Failed to store empty file: "),
    INVALID_PATH("00.09", "Cannot store file with relative path outside current directory: "),
    INVALID_FILE_ID("00.10", "Invalid file id: "),
    NO_FILE_ON_SERVER("00.11", "No file on server: "),
    FILE_IS_CLOSED("00.12", "File is closed: "),
    MAX_REQUESTS_NUMBER("00.13", "Maximum number of requests exceeded."),
    FILES_NOT_FOUND("00.01", "Files not found: "),
    CONTEXT("20.01", "Context parameter not found: ");
}
