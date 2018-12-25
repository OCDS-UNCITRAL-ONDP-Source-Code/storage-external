package com.procurement.storage.model.dto.registration

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.storage.databinding.JsonDateSerializer
import java.time.LocalDateTime
import javax.validation.Valid

data class DocumentsRq @JsonCreator constructor(

        @Valid
        val documents: List<Document>
)

data class Document @JsonCreator constructor(

        val id: String,

        val documentType: String?,

        val title: String?,

        val description: String?,

        var url: String?,

        @JsonSerialize(using = JsonDateSerializer::class)
        var datePublished: LocalDateTime?,

        @JsonSerialize(using = JsonDateSerializer::class)
        val dateModified: LocalDateTime?,

        val format: String?,

        val language: String?,

        val relatedLots: List<String>?,

        val relatedConfirmations: List<String>?
)