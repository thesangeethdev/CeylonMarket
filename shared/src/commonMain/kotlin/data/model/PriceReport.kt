package data.model

import kotlinx.serialization.Serializable

@Serializable
data class PriceReport(
    val date: String,
    val data: ReportData
)

@Serializable
data class ReportData(
    val summary: String? = null,
    val vegetables: List<PriceItem>? = null,
    val rice: List<PriceItem>? = null,
    val fish: List<PriceItem>? = null,
    val other: List<PriceItem>? = null,

)

@Serializable
data class PriceItem(
    val name: String,
    val market: String? = null,
    val priceToday: Double? = null,
    val priceYesterday: Double? = null,
    val unit: String = "Rs/kg",
    val trend: Trend? = null
)

enum class Trend{
    UP, DOWN, STABLE
}
