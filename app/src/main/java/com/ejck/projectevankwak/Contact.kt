package com.ejck.projectevankwak

import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("name") val name: String,
    @SerializedName("info") val info: String
)