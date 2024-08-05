import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import models.Pessoa
import network.ApiConfig
import network.HttpClientSingleton


object PessoaService {
    private val client = HttpClientSingleton.instance
    const val url =
        ApiConfig.Endpoints.PESSOAS

    suspend fun getPessoas(): Result<List<Pessoa>> {
        return runCatching {
            try {
                val response = client.get("$url")
                if (response.status == HttpStatusCode.OK) {
                    response.body<List<Pessoa>>()
                } else {
                    throw Exception("Failed to fetch pessoas: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch pessoas: ${e.message}")
            }
        }
    }

    suspend fun getAniversariantesDoMe(mes: Int): Result<List<Pessoa>> {
        return runCatching {
            try {
                val response = client.get("$url/aniversariantes?mes=$mes")
                if (response.status == HttpStatusCode.OK) {
                    response.body<List<Pessoa>>()
                } else {
                    throw Exception("Failed to fetch pessoas: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch pessoas: ${e.message}")
            }
        }
    }

    suspend fun getPessoasByGrupoId(pessoaId: Int): Result<List<Pessoa>> {
        return runCatching {
            try {
                val response = client.get("$url?grupoId=$pessoaId")
                if (response.status == HttpStatusCode.OK) {
                    response.body<List<Pessoa>>()
                } else {
                    throw Exception("Failed to fetch pessoas: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch pessoas: ${e.message}")
            }
        }
    }

    suspend fun removeGrupoToPessoa(pessoa: Pessoa, cursoId: Int): Result<Boolean> {
        return runCatching {
            try {
                val response = client.put("$url/${pessoa.id}/removeGrupo/$cursoId")
                if (response.status == HttpStatusCode.OK) {
                    true
                } else {
                    throw Exception("Failed to remove Grupo to pessoas: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to remove Grupo to pessoas: ${e.message}")
            }
        }
    }

    suspend fun addGrupoToPessoa(pessoa: Pessoa, cursoId: Int): Result<Pessoa> {
        return runCatching {
            try {
                val response = client.put("$url/${pessoa.id}/addGrupo/$cursoId")
                if (response.status == HttpStatusCode.OK) {
                    pessoa
                } else {
                    throw Exception("Failed to add Grupo to pessoas: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to add Grupo to pessoas: ${e.message}")
            }
        }
    }

    suspend fun removeCursoToPessoa(pessoa: Pessoa, cursoId: Int): Result<Boolean> {
        return runCatching {
            try {
                val response = client.put("$url/${pessoa.id}/removeCurso/$cursoId")
                if (response.status == HttpStatusCode.OK) {
                    true
                } else {
                    throw Exception("Failed to remove curso to pessoas: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to remove curso to pessoas: ${e.message}")
            }
        }
    }

    suspend fun addCursoToPessoa(pessoa: Pessoa, cursoId: Int): Result<Pessoa> {
        return runCatching {
            try {
                val response = client.put("$url/${pessoa.id}/addCurso/$cursoId")
                if (response.status == HttpStatusCode.OK) {
                    pessoa
                } else {
                    throw Exception("Failed to add curso to pessoas: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to add curso to pessoas: ${e.message}")
            }
        }
    }

    suspend fun createPessoa(pessoa: Pessoa): Result<Pessoa> {
        return runCatching {
            try {
                val response = client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(pessoa)
                }
                if (response.status == HttpStatusCode.OK) {
                    pessoa
                } else {
                    throw Exception("Failed to create pessoas: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to create pessoas: ${e.message}")
            }
        }
    }
}
