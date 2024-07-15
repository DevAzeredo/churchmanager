package models

import kotlinx.serialization.Serializable

@Serializable
data class PequenoGrupo(
    val id: Int,
    val nome: String,
    val descricao: String,
    val rua: String,
    val numero: String,
    val bairro: String,
    val cidade: String,
    val cep: String,
    val frequencia: String
)