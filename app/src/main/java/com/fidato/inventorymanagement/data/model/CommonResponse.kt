package com.fidato.inventorymanagement.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommonResponse(
    @JsonProperty("message")
    private val message: String?
)