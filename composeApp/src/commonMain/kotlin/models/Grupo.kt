package models

import kotlinx.serialization.Serializable

@Serializable
data class Grupo (
    val id: Int,
    val nome: String,
    val descricao: String,
    val pessoas: List<Pessoa>
)