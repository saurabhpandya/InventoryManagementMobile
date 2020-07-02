package com.fidato.inventorymanagement.data.model.master

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SubCategory(
    @JsonProperty("id")
    private val id: Int,
    private val name: String,
    @JsonProperty("cat_id")
    private val catId: Int,
    @JsonProperty("sub_cat_id")
    private val subCatId: Int
)