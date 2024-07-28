package network

object ApiConfig {
    const val BASE_URL = "http://localhost:9090/api"

    object Endpoints {
        const val CURSOS = "$BASE_URL/cursos"
        const val PESSOAS = "$BASE_URL/pessoas"
    }
}