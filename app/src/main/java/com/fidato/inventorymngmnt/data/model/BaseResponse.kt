package com.fidato.inventorymngmnt.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BaseResponse<T>(
    @JsonProperty("data")
    val data: T? = null,
    @JsonProperty("errorResponse")
    val error: ErrorResponse? = null
)