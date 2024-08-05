package services

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import models.Curso
import network.ApiConfig
import network.HttpClientSingleton

object CursoService {
    private val client = HttpClientSingleton.instance
    private const val cURL =
        ApiConfig.Endpoints.CURSOS

    suspend fun getCursos(): Result<List<Curso>> {
        return runCatching {
            try {
                val response = client.get(cURL)
                if (response.status == HttpStatusCode.OK) {
                    response.body<List<Curso>>()
                } else {
                    throw Exception("Failed to fetch cursos: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch cursos: ${e.message}")
            }
        }
    }

    suspend fun proximosCursos(limit:Int): Result<List<Curso>> {
        return runCatching {
            try {
                val response = client.get("$cURL/proximos?limit=$limit")
                if (response.status == HttpStatusCode.OK) {
                    response.body<List<Curso>>()
                } else {
                    throw Exception("Failed to fetch cursos: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch cursos: ${e.message}")
            }
        }
    }

    suspend fun getCursosByPessoaId(pessoaId: Int): Result<List<Curso>> {
        return runCatching {
            try {
                val response = client.get("$cURL?pessoaId=$pessoaId")
                if (response.status == HttpStatusCode.OK) {
                    response.body<List<Curso>>()
                } else {
                    throw Exception("Failed to fetch cursos: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch cursos: ${e.message}")
            }
        }
    }

    suspend fun createCurso(curso: Curso): Result<Curso> {
        return runCatching {
            try {
                val response = client.post(cURL) {
                    contentType(ContentType.Application.Json)
                    setBody(curso)
                }
                if (response.status == HttpStatusCode.OK) {
                    response.body<Curso>()
                } else {
                    throw Exception("Failed to create cursos: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to create cursos: ${e.message}")
            }
        }
    }

    suspend fun updateCurso(id: Int, curso: Curso): Curso {
        return client.put("$cURL/$id") {
            setBody(curso)
        }.body()
    }

    suspend fun deleteCurso(id: Int) {
        client.delete("$cURL/$id")
    }
}
