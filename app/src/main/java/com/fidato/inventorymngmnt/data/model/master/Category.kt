package com.fidato.inventorymngmnt.data.model.master

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
data class Category(
    @JsonProperty("id")
    var id: Int? = null,
    @JsonProperty("name")
    var name: String = ""
) : Parcelable