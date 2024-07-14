import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

object PessoaService {
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getPessoas(): List<Pessoa> {
        return client.get("https://api.suaigreja.com/pessoas")
    }

    suspend fun createPessoa(pessoa: Pessoa): Pessoa {
        return client.post("https://api.suaigreja.com/pessoas") {
            body = pessoa
        }
    }

    suspend fun updatePessoa(id: Int, pessoa: Pessoa): Pessoa {
        return client.put("https://api.suaigreja.com/pessoas/$id") {
            body = pessoa
        }
    }

    suspend fun deletePessoa(id: Int) {
        client.delete<Unit>("https://api.suaigreja.com/pessoas/$id")
    }
}
