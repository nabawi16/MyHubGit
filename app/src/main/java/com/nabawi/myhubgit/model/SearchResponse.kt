package com.nabawi.myhubgit.model

import com.squareup.moshi.Json

data class SearchResponse(
    @field:Json(name = "items")
    val items: List<User>
)
