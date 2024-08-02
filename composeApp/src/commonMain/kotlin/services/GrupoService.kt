
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import models.Grupo
import network.ApiConfig
import network.HttpClientSingleton

object GrupoService {
    private val client = HttpClientSingleton.instance
    const val url =
        ApiConfig.Endpoints.GRUPOS

    suspend fun getGrupos(): List<Grupo> {
        return client.get("https://api.suaigreja.com/pequenosgrupos").body()

    }

    suspend fun createPequenoGrupo(pequenoGrupo: Grupo): Grupo {
        return client.post("https://api.suaigreja.com/pequenosgrupos") {
            setBody(pequenoGrupo)
        }.body()
    }

    suspend fun updatePequenoGrupo(id: Int, pequenoGrupo: Grupo): Grupo {
        return client.put("https://api.suaigreja.com/pequenosgrupos/$id") {
            setBody(pequenoGrupo)
        }.body()
    }

    suspend fun deletePequenoGrupo(id: Int) {
        client.delete("https://api.suaigreja.com/pequenosgrupos/$id")
    }
}
