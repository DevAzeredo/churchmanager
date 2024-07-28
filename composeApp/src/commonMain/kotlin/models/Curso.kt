package models

import kotlinx.serialization.Serializable

@Serializable
data class Curso(
    val id: Int,
    val nome: String,
    val descricao: String,
    val dataInicio: String,
    val dataFim: String
)