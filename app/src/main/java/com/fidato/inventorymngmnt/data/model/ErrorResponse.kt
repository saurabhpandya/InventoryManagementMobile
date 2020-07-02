package com.fidato.inventorymngmnt.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ErrorResponse(
    @JsonProperty("errorCode")
    val errorCode: Int?,
    @JsonProperty("errorMessage")
    val errorMessage: String?
)