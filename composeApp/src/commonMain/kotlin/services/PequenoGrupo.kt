import io.ktor.client.call.body
import io.ktor.client.request.*
import models.PequenoGrupo
import network.HttpClientSingleton

object PequenoGrupoService {
    private val client = HttpClientSingleton.instance

    suspend fun getPequenosGrupos(): List<PequenoGrupo> {
        return client.get("https://api.suaigreja.com/pequenosgrupos").body()
    }

    suspend fun createPequenoGrupo(pequenoGrupo: PequenoGrupo): PequenoGrupo {
        return client.post("https://api.suaigreja.com/pequenosgrupos") {
            setBody(pequenoGrupo)
        }.body()
    }

    suspend fun updatePequenoGrupo(id: Int, pequenoGrupo: PequenoGrupo): PequenoGrupo {
        return client.put("https://api.suaigreja.com/pequenosgrupos/$id") {
            setBody(pequenoGrupo)
        }.body()
    }

    suspend fun deletePequenoGrupo(id: Int) {
        client.delete("https://api.suaigreja.com/pequenosgrupos/$id")
    }
}
