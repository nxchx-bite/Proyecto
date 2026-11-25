package com.example.dsy1105_009v_2026.repository

import com.example.dsy1105_009v_2026.data.remote.RetrofitInstance
import com.example.dsy1105_009v_2026.model.Post

class PostRepository {

    suspend fun getPosts(): List<Post> {
        return RetrofitInstance.api.getPosts()
    }
}
