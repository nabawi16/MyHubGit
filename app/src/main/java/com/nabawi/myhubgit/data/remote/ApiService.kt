package com.nabawi.myhubgit.data.remote

import com.nabawi.myhubgit.model.SearchResponse
import com.nabawi.myhubgit.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchUsers (
        @Query("q")
        query: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser (
        @Path("username")
        username: String
    ): Call<User>

    @GET("users/{username}/followers")
    fun getUserFollowers (
        @Path("username")
        username: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getUserFollowing (
        @Path("username")
        username: String
    ): Call<List<User>>
}