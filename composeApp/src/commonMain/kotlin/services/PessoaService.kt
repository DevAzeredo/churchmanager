import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
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

    suspend fun createPessoa(pessoa: Pessoa): Pessoa {
        client.post("https://api.suaigreja.com/pessoas") {
        }
        return pessoa
    }

    suspend fun updatePessoa(id: Int, pessoa: Pessoa): Pessoa {
        return client.put("https://api.suaigreja.com/pessoas/$id") {
            setBody(pessoa)
        }.body()
    }

    suspend fun deletePessoa(id: Int) {
        client.delete("https://api.suaigreja.com/pessoas/$id")
    }
}
