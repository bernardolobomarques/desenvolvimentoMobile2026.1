package com.example.consumirapicrud.api

import com.google.gson.annotations.SerializedName

data class Device(
    val id: Int? = null,
    val name: String,
    val type: String,
    @SerializedName("is_active")
    val isActive: Boolean
)
