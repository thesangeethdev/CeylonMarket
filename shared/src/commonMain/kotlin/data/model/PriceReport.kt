//package data.model
//
//import kotlinx.serialization.Serializable
//
//@Serializable
//data class PriceReport(
//    val date: String,
//    val data: ReportData
//)
//
//@Serializable
//data class ReportData(
//    val summary: String? = null,
//    val vegetables: List<PriceItem>? = null,
//    val rice: List<PriceItem>? = null,
//    val fish: List<PriceItem>? = null,
//    val other: List<PriceItem>? = null,
//
//)
//
//@Serializable
//data class PriceItem(
//    val name: String,
//    val market: String? = null,
//    val priceToday: Double? = null,
//    val priceYesterday: Double? = null,
//    val unit: String = "Rs/kg",
//    val trend: Trend? = null
//)
//
//enum class Trend{
//    UP, DOWN, STABLE
//}


package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceReport(
    val date: String,
    val data: ReportData
)

@Serializable
data class ReportData(
    val summary: String? = null,
    @SerialName("reportDate") val reportDate: String? = null,
    val sections: List<Section> = emptyList()
) {
    // Helper properties — maps sections back to your old field names
    val vegetables: List<PriceItem>?
        get() = sections.find { it.sectionName.equals("Vegetables", ignoreCase = true) }?.items?.map { it.toPriceItem() }

    val rice: List<PriceItem>?
        get() = sections.find { it.sectionName.equals("Rice", ignoreCase = true) }?.items?.map { it.toPriceItem() }

    val fish: List<PriceItem>?
        get() = sections.find { it.sectionName.equals("Fish", ignoreCase = true) }?.items?.map { it.toPriceItem() }

    val other: List<PriceItem>?
        get() = sections.find { it.sectionName.equals("Other", ignoreCase = true) }?.items?.map { it.toPriceItem() }

    val fruits: List<PriceItem>?
        get() = sections.find { it.sectionName.equals("Fruits", ignoreCase = true) }?.items?.map { it.toPriceItem() }
}

@Serializable
data class Section(
    @SerialName("sectionName") val sectionName: String,
    val items: List<MarketItem> = emptyList()
)

@Serializable
data class MarketItem(
    val item: String,
    val unit: String,
    val retail: MarketPrices? = null,
    val wholesale: MarketPrices? = null
) {
    fun toPriceItem(): PriceItem {
        // Average of available retail "today" prices
        val todayPrices = listOfNotNull(
            retail?.pettah?.today,
            retail?.dambulla?.today,
            retail?.narahenpita?.today,
            retail?.negombo?.today,
            retail?.peliyagoda?.today,
            retail?.marandagahamula?.today
        )
        val yesterdayPrices = listOfNotNull(
            retail?.pettah?.yesterday,
            retail?.dambulla?.yesterday,
            retail?.narahenpita?.yesterday,
            retail?.negombo?.yesterday,
            retail?.peliyagoda?.yesterday,
            retail?.marandagahamula?.yesterday
        )

        val avgToday = todayPrices.takeIf { it.isNotEmpty() }?.average()
        val avgYesterday = yesterdayPrices.takeIf { it.isNotEmpty() }?.average()

        val trend = when {
            avgToday == null || avgYesterday == null -> null
            avgToday > avgYesterday -> Trend.UP
            avgToday < avgYesterday -> Trend.DOWN
            else -> Trend.STABLE
        }

        // First market with a price
        val marketName = when {
            retail?.pettah?.today != null -> "Pettah"
            retail?.dambulla?.today != null -> "Dambulla"
            retail?.narahenpita?.today != null -> "Narahenpita"
            retail?.negombo?.today != null -> "Negombo"
            retail?.peliyagoda?.today != null -> "Peliyagoda"
            retail?.marandagahamula?.today != null -> "Marandagahamula"
            else -> null
        }

        return PriceItem(
            name = item,
            market = marketName,
            priceToday = avgToday,
            priceYesterday = avgYesterday,
            unit = unit,
            trend = trend
        )
    }
}

@Serializable
data class MarketPrices(
    val pettah: PriceDay? = null,
    val dambulla: PriceDay? = null,
    val narahenpita: PriceDay? = null,
    val negombo: PriceDay? = null,
    val peliyagoda: PriceDay? = null,
    val marandagahamula: PriceDay? = null
)

@Serializable
data class PriceDay(
    val today: Double? = null,
    val yesterday: Double? = null
)

// Kept unchanged for UI compatibility
@Serializable
data class PriceItem(
    val name: String,
    val market: String? = null,
    val priceToday: Double? = null,
    val priceYesterday: Double? = null,
    val unit: String = "Rs/kg",
    val trend: Trend? = null
)

@Serializable
enum class Trend {
    UP, DOWN, STABLE
}