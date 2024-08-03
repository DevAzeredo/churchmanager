package models

import kotlinx.serialization.Serializable

@Serializable
data class Grupo (
    val id: Int,
    var nome: String,
    var descricao: String,
)