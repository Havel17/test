package com.example.test.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoryService {
    @GET("/v1/gifs/trending")
    suspend fun trending(
        @Query("api_key") api_key: String,
        @Query("limit") limit: Int,
        @Query("rating") rating: String
    ): String

    @GET("/v1/gifs/search")
    suspend fun search(
        @Query("api_key") api_key: String,
        @Query("q") term: String,
        @Query("limit") limit: Int,
        @Query("lang") lang: String
    ): String
}