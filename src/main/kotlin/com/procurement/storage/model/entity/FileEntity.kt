package com.procurement.storage.model.entity

import java.util.*

data class FileEntity(

        var id: String,

        var isOpen: Boolean,

        var dateModified: Date?,

        var datePublished: Date?,

        var hash: String,

        var weight: Long,

        var fileName: String,

        var fileOnServer: String?,

        var owner: String?

)
