package com.procurement.storage.model.dto.registration

import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.core.io.ByteArrayResource

data class FileDataRs @JsonCreator constructor(

        val fileName: String,

        val resource: ByteArrayResource
)
