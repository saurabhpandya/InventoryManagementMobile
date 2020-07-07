package com.fidato.inventorymngmnt.data.model.master

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductVarient(
    @JsonProperty("id")
    val id: Int = -1,
    @JsonProperty("productId")
    val productId: Int = -1,
    @JsonProperty("size")
    val size: Double = 0.0,
    @JsonProperty("color")
    val color: String = "",
    @JsonProperty("quantity")
    val quantity: Int = -1,
    @JsonProperty("price")
    val price: Double = 0.0,
    @JsonProperty("active")
    val active: Boolean = false,
    @JsonProperty("deleted")
    val deleted: Boolean = false,
    @JsonProperty("blocked")
    val blocked: Boolean = false

)