package com.example.dsy1105_009v_2026.data.remote

import com.example.dsy1105_009v_2026.model.Post
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): List<Post>
}
