package services

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import models.Curso
import network.ApiConfig
import network.HttpClientSingleton

object CursoService {
    private val client = HttpClientSingleton.instance
    const val url =
        ApiConfig.Endpoints.CURSOS

    suspend fun getCursos(): List<Curso> {
//        return client.get("https://api.suaigreja.com/cursos").body()
        return listOf(
            Curso(
                id = 1,
                nome = "Curso de Teologia",
                descricao = "Um curso abrangente sobre teologia.",
                data = "1998"
            ),
            Curso(
                id = 2,
                nome = "Curso de Música",
                descricao = "Aprenda a tocar e cantar músicas religiosas.",
                data = "1998"
            ),
            Curso(
                id = 3,
                nome = "Curso de Liderança",
                descricao = "Desenvolva habilidades de liderança para a comunidade.",
                data = "1998"
            )
        )
    }

    suspend fun createCurso(curso: Curso): Curso {
        return curso
//        client.post(url) {
//            setBody(curso)
//        }.body()
    }

    suspend fun updateCurso(id: Int, curso: Curso): Curso {
        return client.put("$url/$id") {
            setBody(curso)
        }.body()
    }

    suspend fun deleteCurso(id: Int) {
        client.delete("$url/$id")
    }
}
