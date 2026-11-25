package com.example.dsy1105_009v_2026.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.dsy1105_009v_2026.model.SolicitudRemota
import com.example.dsy1105_009v_2026.repository.SolicitudesRemotasRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SolicitudesRemotasViewModel : ViewModel() {

    private val repository = SolicitudesRemotasRepository()

    private val _solicitudes =
        MutableStateFlow<List<SolicitudRemota>>(emptyList())

    val solicitudes: StateFlow<List<SolicitudRemota>> =
        _solicitudes

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarSolicitudes()
    }

    fun cargarSolicitudes() {
        viewModelScope.launch {

            _cargando.value = true
            _error.value = null

            try {
                _solicitudes.value =
                    repository.obtenerSolicitudes()

            } catch (e: Exception) {
                _error.value =
                    "Error al consultar Spring Boot: " +
                            (e.message ?: "Error desconocido")

            } finally {
                _cargando.value = false
            }
        }
    }
}
