package network

object ApiConfig {
    const val BASE_URL = "http://127.0.0.1:9090/api"

    object Endpoints {
        const val CURSOS = "$BASE_URL/cursos"
        const val PESSOAS = "$BASE_URL/pessoas"
        const val GRUPOS = "$BASE_URL/grupos"
    }
}