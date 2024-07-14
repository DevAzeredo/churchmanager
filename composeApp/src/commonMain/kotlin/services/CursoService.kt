package services
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

object CursoService {
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getCursos(): List<Curso> {
        return client.get("https://api.suaigreja.com/cursos")
    }

    suspend fun createCurso(curso: Curso): Curso {
        return client.post("https://api.suaigreja.com/cursos") {
            body = curso
        }
    }

    suspend fun updateCurso(id: Int, curso: Curso): Curso {
        return client.put("https://api.suaigreja.com/cursos/$id") {
            body = curso
        }
    }

    suspend fun deleteCurso(id: Int) {
        client.delete<Unit>("https://api.suaigreja.com/cursos/$id")
    }
}
