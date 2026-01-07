package com.jason.alp_vp.data.dto.post

import com.google.gson.annotations.SerializedName

// Wrapper for API responses - backend wraps data in { "data": ... }
data class PostResponse(
    @SerializedName("data") val data: List<PostResponseItem>
)

