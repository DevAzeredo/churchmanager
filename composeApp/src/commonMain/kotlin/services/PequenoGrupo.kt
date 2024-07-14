import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

object PequenoGrupoService {
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getPequenosGrupos(): List<PequenoGrupo> {
        return client.get("https://api.suaigreja.com/pequenosgrupos")
    }

    suspend fun createPequenoGrupo(pequenoGrupo: PequenoGrupo): PequenoGrupo {
        return client.post("https://api.suaigreja.com/pequenosgrupos") {
            body = pequenoGrupo
        }
    }

    suspend fun updatePequenoGrupo(id: Int, pequenoGrupo: PequenoGrupo): PequenoGrupo {
        return client.put("https://api.suaigreja.com/pequenosgrupos/$id") {
            body = pequenoGrupo
        }
    }

    suspend fun deletePequenoGrupo(id: Int) {
        client.delete<Unit>("https://api.suaigreja.com/pequenosgrupos/$id")
    }
}
