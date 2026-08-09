package data.model.api

import data.model.PriceReport
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val baseUrl = "https://market-prices-lk.onrender.com"

    suspend fun getLatest(): PriceReport {
        return client.get("$baseUrl/latest").body()
    }
    suspend fun getReports(): List<String> {
        val response: String = client.get("$baseUrl/reports").body()
        return response.lines().filter { it.isNotBlank() }
    }
    suspend fun getReport(date: String): PriceReport{
        return client.get("$baseUrl/reports/$date").body()
    }
    suspend fun getHistory(): List<PriceReport>{
        return client.get("$baseUrl/history").body()
    }
}