package com.fidato.inventorymngmnt.data.model

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
data class Supplier(
    @JsonProperty("id")
    var id: Int? = null,
    @JsonProperty("name")
    var name: String = "",
    @JsonProperty("cat_id")
    var catId: Int? = null,
    @JsonProperty("email")
    var email: String = "",
    @JsonProperty("password")
    var password: String = "",
    @JsonProperty("active")
    var active: Boolean = false,
    @JsonProperty("deleted")
    var deleted: Boolean = false,
    @JsonProperty("blocked")
    var blocked: Boolean = false
) : Parcelable