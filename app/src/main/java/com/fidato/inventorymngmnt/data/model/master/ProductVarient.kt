package com.fidato.inventorymngmnt.data.model.master

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true, value = arrayOf("selected"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@Parcelize
data class ProductVarient(
    @JsonProperty("id")
    var id: Int? = null,
    @JsonProperty("productId")
    var productId: Int? = -1,
    @JsonProperty("size")
    var size: String? = null,
    @JsonProperty("color")
    var color: String? = null,
    @JsonProperty("quantity")
    var quantity: Int? = null,
    @JsonProperty("price")
    var price: Double? = null,
    @JsonProperty("active")
    var active: Boolean = false,
    @JsonProperty("deleted")
    var deleted: Boolean = false,
    @JsonProperty("blocked")
    var blocked: Boolean = false,
    @JsonProperty("selected")
    var selected: Boolean = false
) : Parcelable