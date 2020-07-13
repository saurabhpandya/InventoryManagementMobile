package com.fidato.inventorymngmnt.data.model.master

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
data class SubCategory(
    @JsonProperty("id")
    var id: Int? = null,
    @JsonProperty("name")
    var name: String = "",
    @JsonProperty("cat_id")
    var catId: Int = -1,
    @JsonProperty("sub_cat_id")
    var subCatId: Int? = null
) : Parcelable