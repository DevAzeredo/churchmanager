package models

import kotlinx.serialization.Serializable

@Serializable
data class Pessoa(
    val id: Int,
    val nome: String,
    val dataNascimento: String,
    val endereco: String,
    val telefone: String,
    val email: String,
    val profissao: String,
)
