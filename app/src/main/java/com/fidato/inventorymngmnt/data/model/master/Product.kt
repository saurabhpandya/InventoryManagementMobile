package com.fidato.inventorymngmnt.data.model.master

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Parcelize
data class Product(
    @JsonProperty("id")
    var id: Int? = null,
    @JsonProperty("name")
    var name: String? = null,
    @JsonProperty("description")
    var description: String? = null,
    @JsonProperty("subCatId")
    var subCatId: Int? = -1,
    @JsonProperty("supplierId")
    var supplierId: Int? = -1,
    @JsonProperty("price")
    var price: Double? = null,
    @JsonProperty("currencyId")
    var currencyId: Int? = null,
    @JsonProperty("quantity")
    var quantity: Int? = null,
    @JsonProperty("active")
    var active: Boolean = false,
    @JsonProperty("deleted")
    var deleted: Boolean = false,
    @JsonProperty("blocked")
    var blocked: Boolean = false,
    @JsonProperty("productVariantMapping")
    var productVariantMapping: ArrayList<ProductVarient> = ArrayList<ProductVarient>()


) : Parcelable