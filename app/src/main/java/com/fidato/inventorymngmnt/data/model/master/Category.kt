package com.fidato.inventorymngmnt.data.model.master

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
    @JsonProperty("id")
    var id: String = "",
    @JsonProperty("name")
    var name: String = ""
)