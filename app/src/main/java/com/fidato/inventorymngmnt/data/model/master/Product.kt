package com.fidato.inventorymngmnt.data.model.master

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Product(
    @JsonProperty("id")
    val id: Int = -1,
    @JsonProperty("name")
    val name: String = "",
    @JsonProperty("description")
    val description: String = "",
    @JsonProperty("subCatId")
    val subCatId: Int = -1,
    @JsonProperty("supplierId")
    val supplierId: Int = -1,
    @JsonProperty("price")
    val price: Double = 0.0,
    @JsonProperty("currencyId")
    val currencyId: Int = -1,
    @JsonProperty("quantity")
    val quantity: Int = -1,
    @JsonProperty("active")
    val active: Boolean = false,
    @JsonProperty("deleted")
    val deleted: Boolean = false,
    @JsonProperty("blocked")
    val blocked: Boolean = false
)