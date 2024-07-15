import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import models.Pessoa
import network.HttpClientSingleton

object PessoaService {
    private val client = HttpClientSingleton.instance


    suspend fun getPessoas(): List<Pessoa> {
        return client.get("https://api.suaigreja.com/pessoas").body()
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
