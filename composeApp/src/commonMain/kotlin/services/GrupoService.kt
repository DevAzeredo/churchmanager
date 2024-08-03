
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import models.Grupo
import network.ApiConfig
import network.HttpClientSingleton

object GrupoService {
    private val client = HttpClientSingleton.instance
    const val url =
        ApiConfig.Endpoints.GRUPOS

    suspend fun getGrupos(): Result<List<Grupo>> {
        return runCatching {
            try {
                val response = client.get("$url")
                if (response.status == HttpStatusCode.OK) {
                    response.body<List<Grupo>>()
                } else {
                    throw Exception("Failed to fetch Grupos: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch Grupos: ${e.message}")
            }
        }
    }

    suspend fun createGrupo(grupo: Grupo): Result<Grupo> {
        return runCatching {
            try {
                val response = client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(grupo)
                }
                if (response.status == HttpStatusCode.OK) {
                    grupo
                } else {
                    throw Exception("Failed to create grupo: ${response.status}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to create grupo: ${e.message}")
            }
        }
    }
}
