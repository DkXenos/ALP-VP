package com.jason.alp_vp.data.dto.post

import com.google.gson.annotations.SerializedName

data class PostRequest(
    @SerializedName("content") val content: String,
    @SerializedName("image") val image: String? = null
)
