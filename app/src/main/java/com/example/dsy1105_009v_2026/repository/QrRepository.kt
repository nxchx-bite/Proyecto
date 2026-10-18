package com.example.dsy1105_009v_2026.repository

import com.example.dsy1105_009v_2026.model.QrResult

class QrRepository {
    fun processQrContent(content: String): QrResult {
        // Aquí podrías guardar o procesar el QR en una BD o API
        return QrResult(content)
    }
}