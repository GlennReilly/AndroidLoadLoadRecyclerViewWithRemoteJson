package com.glennreilly.androidproficiencyexercise.models

import com.google.gson.annotations.SerializedName


class Facts {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("rows")
    var factRows: List<FactRow>? = null
}

class FactRow {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("imageHref")
    var imageHref: Any? = null
}