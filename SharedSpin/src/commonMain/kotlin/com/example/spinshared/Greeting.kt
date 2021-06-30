package com.example.spinshared

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}

@Serializable
data class NetworkResult(val network: Network)

@Serializable
data class NetworkListResult(val networks: List<Network>)

@Serializable
data class Network(val id: String, val name: String, val location: Location, val stations: List<Station> = emptyList())

@Serializable
data class Location(val city: String, val country: String, val latitude: Double, val longitude: Double)

@Serializable
data class Station(val id: String? = "", val name: String,
                   val empty_slots: Int? = 0, val free_bikes: Int? = 0,
                   val latitude: Double, val longitude: Double) {}


fun Station.freeBikes(): Int {
    return free_bikes?: 0
}
fun Station.slots(): Int {
    return (empty_slots ?: 0) + (free_bikes ?: 0)
}

class CityBikesApi(private val client: HttpClient,
                   private val baseUrl: String = "https://api.citybik.es/v2/networks"
) {

    suspend fun fetchNetworkList(): NetworkListResult {
        return client.get(baseUrl)
    }

    suspend fun fetchBikeShareInfo(network: String): NetworkResult {
        return client.get("$baseUrl/$network")
    }
}

//////

@Serializable
//data class NetworkList(override val id: String, val networks: List<Network>): Metadata

@ExperimentalCoroutinesApi
class CityBikesRepository: KoinComponent {
    private val cityBikesApi: CityBikesApi by inject()
    private val coroutineScope: CoroutineScope = MainScope()

    private val _groupedNetworkList = MutableStateFlow<Map<String,List<Network>>>(emptyMap())
    val groupedNetworkList: StateFlow<Map<String, List<Network>>> = _groupedNetworkList

    var networkList: List<Network> = emptyList()

    init {
        coroutineScope.launch {
            fetchAndStoreNetworkList()
        }
    }

    private suspend fun fetchAndStoreNetworkList() {
        networkList = cityBikesApi.fetchNetworkList().networks
    }

    @Throws(Exception::class)
    suspend fun fetchBikeShareInfo(network: String) : List<Station> {
        val result = cityBikesApi.fetchBikeShareInfo(network)
        return result.network.stations
    }

    @Throws(Exception::class)
    suspend fun fetchGroupedNetworkList(success: (Map<String, List<Network>>) -> Unit)  {
        coroutineScope.launch {
            groupedNetworkList.collect {
                success(it)
            }
        }
    }

    @Throws(Exception::class)
    suspend fun fetchNetworkList(success: (List<Network>) -> Unit)  {
        coroutineScope.launch {
            success(networkList)
        }
    }

    @Throws(Exception::class)
    suspend fun fetchNetworkListNew(success: (List<Network>) -> Unit)  {
        val networks = cityBikesApi.fetchNetworkList().networks
        coroutineScope.launch {
//            success(networks)
        }
        success(networks)
    }

    fun pollNetworkUpdates(network: String): Flow<List<Station>> = flow {
        while (true) {
            val stations = cityBikesApi.fetchBikeShareInfo(network).network.stations
            emit(stations)
            delay(POLL_INTERVAL)
        }
    }

    companion object {
        private const val POLL_INTERVAL = 10000L
    }
}