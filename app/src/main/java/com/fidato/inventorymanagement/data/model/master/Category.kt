package com.fidato.inventorymanagement.data.model.master

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val name: String
)