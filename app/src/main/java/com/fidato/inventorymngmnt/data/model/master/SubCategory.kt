package com.fidato.inventorymngmnt.data.model.master

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SubCategory(
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("cat_id")
    val catId: Int?,
    @JsonProperty("sub_cat_id")
    val subCatId: Int?
)