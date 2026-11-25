package com.example.dsy1105_009v_2026.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsy1105_009v_2026.model.Post
import com.example.dsy1105_009v_2026.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        obtenerPosts()
    }

    fun obtenerPosts() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                _posts.value = repository.getPosts()
            } catch (e: Exception) {
                _error.value = "Error al obtener los datos"
            } finally {
                _cargando.value = false
            }
        }
    }
}

