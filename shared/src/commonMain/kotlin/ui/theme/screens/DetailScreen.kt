package ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.PriceItem
import data.model.PriceReport
import ui.theme.ErrorRed
import ui.theme.OnSurface
import ui.theme.OnSurfaceVariant
import ui.theme.SuccessGreen
import ui.theme.Surface
import ui.theme.TealPrimary
import viewmodel.DetailUiState
import viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedNavIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val state = uiState as? DetailUiState.Success
                    Text(
                        state?.report?.date?.let { formatDate(it) } ?: "Loading...",
                        color = Surface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Surface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TealPrimary)
            )
        },
        bottomBar = {
            if (uiState is DetailUiState.Success) {
                NavigationBar(
                    containerColor = Surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = selectedNavIndex == 0,
                        onClick = { selectedNavIndex = 0 },
                        icon = { Icon(Icons.Default.Summarize, contentDescription = "Summary") },
                        label = { Text("Summary") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            indicatorColor = TealPrimary.copy(alpha = 0.12f),
                            unselectedIconColor = OnSurfaceVariant,
                            unselectedTextColor = OnSurfaceVariant
                        )
                    )
                    NavigationBarItem(
                        selected = selectedNavIndex == 1,
                        onClick = { selectedNavIndex = 1 },
                        icon = { Icon(Icons.Default.BarChart, contentDescription = "Prices") },
                        label = { Text("Prices") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            indicatorColor = TealPrimary.copy(alpha = 0.12f),
                            unselectedIconColor = OnSurfaceVariant,
                            unselectedTextColor = OnSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (uiState) {
                is DetailUiState.Loading -> LoadingScreen()
                is DetailUiState.Error -> ErrorScreen(
                    (uiState as DetailUiState.Error).message,
                    onRetry = { viewModel.loadReport() }
                )
                is DetailUiState.Success -> {
                    val report = (uiState as DetailUiState.Success).report
                    when (selectedNavIndex) {
                        0 -> SummaryContent(report.data.summary)
                        else -> PricesContent(report)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryContent(summary: String?) {
    if (summary.isNullOrBlank()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No summary available", color = OnSurfaceVariant)
        }
        return
    }

    val entries = remember(summary) { parseSummaryEntries(summary) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(entries) { entry ->
            SummaryEntryCard(entry)
        }
    }
}

@Composable
private fun SummaryEntryCard(entry: SummaryEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                entry.description,
                color = OnSurface,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
            if (entry.trendLines.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                entry.trendLines.forEach { trendLine ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            if (trendLine.isUp) "↑" else "↓",
                            color = if (trendLine.isUp) ErrorRed else SuccessGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.width(24.dp).padding(top = 2.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            trendLine.markets.forEach { market ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        market.marketName,
                                        color = OnSurfaceVariant,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "${market.yesterday.toInt()} → ${market.today.toInt()}",
                                        color = if (trendLine.isUp) ErrorRed else SuccessGreen,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PricesContent(report: PriceReport) {
    val tabItems = listOf(
        TabItem("Vegetables", report.data.vegetables),
        TabItem("Rice", report.data.rice),
        TabItem("Fish", report.data.fish),
        TabItem("Fruits", report.data.fruits),
        TabItem("Others", report.data.other),
    ).filter { !it.items.isNullOrEmpty() }

    var selectedTabIndex by remember { mutableStateOf(0) }

    if (tabItems.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No Price Data Available", color = OnSurfaceVariant)
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Surface,
            contentColor = TealPrimary
        ) {
            tabItems.forEachIndexed { index, tabItem ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            tabItem.title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = TealPrimary,
                    unselectedContentColor = OnSurfaceVariant
                )
            }
        }

        val items = tabItems.getOrNull(selectedTabIndex)?.items ?: emptyList()
        if (items.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { PriceRow(it) }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No items in this category", color = OnSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PriceRow(item: PriceItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.name, fontWeight = FontWeight.Medium, color = OnSurface)
                item.market?.let {
                    Text(it, fontSize = 12.sp, color = OnSurfaceVariant)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                item.priceToday?.let {
                    Text(
                        "Rs ${it.toInt()}",
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                }
                item.trend?.let { trend ->
                    val color = when (trend) {
                        data.model.Trend.UP -> ErrorRed
                        data.model.Trend.DOWN -> SuccessGreen
                        data.model.Trend.STABLE -> OnSurfaceVariant
                    }
                    val arrow = when (trend) {
                        data.model.Trend.UP -> " ↑"
                        data.model.Trend.DOWN -> " ↓"
                        data.model.Trend.STABLE -> " →"
                    }
                    Text(arrow, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = TealPrimary)
    }
}

@Composable
private fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
        Spacer(modifier = Modifier.height(8.dp))
        Text(message, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
        ) {
            Text("Retry")
        }
    }
}

private data class TabItem(val title: String, val items: List<PriceItem>?)

private data class TrendMarketEntry(
    val marketName: String,
    val yesterday: Double,
    val today: Double
)

private data class ParsedTrendLine(
    val isUp: Boolean,
    val markets: List<TrendMarketEntry>
)

private data class SummaryEntry(
    val description: String,
    val trendLines: List<ParsedTrendLine>
)

// Format: "Pettah : Dambulla : 400.00 380.00 600.00 585.00"
// → N market names, then N yesterday prices, then N today prices
private fun parseTrendLine(isUp: Boolean, raw: String): ParsedTrendLine {
    val parts = raw.split(":").map { it.trim() }
    val marketNames = mutableListOf<String>()
    var prices = listOf<Double>()

    for (part in parts) {
        val tokens = part.split(" ").filter { it.isNotBlank() }
        if (tokens.isNotEmpty() && tokens.all { it.toDoubleOrNull() != null }) {
            prices = tokens.map { it.toDouble() }
        } else if (part.isNotBlank()) {
            marketNames.add(part)
        }
    }

    val n = marketNames.size
    val markets = marketNames.mapIndexed { i, name ->
        TrendMarketEntry(
            marketName = name,
            yesterday = prices.getOrElse(i) { 0.0 },
            today = prices.getOrElse(i + n) { 0.0 }
        )
    }
    return ParsedTrendLine(isUp, markets)
}

private fun parseSummaryEntries(summary: String): List<SummaryEntry> {
    val lines = summary.split("\n").filter { it.isNotBlank() }
    val entries = mutableListOf<SummaryEntry>()
    var currentDesc = ""
    val currentTrends = mutableListOf<ParsedTrendLine>()

    for (line in lines) {
        when {
            line.startsWith("↑") -> currentTrends.add(parseTrendLine(true, line.removePrefix("↑").trim()))
            line.startsWith("↓") -> currentTrends.add(parseTrendLine(false, line.removePrefix("↓").trim()))
            else -> {
                if (currentDesc.isNotEmpty()) {
                    entries.add(SummaryEntry(currentDesc, currentTrends.toList()))
                    currentTrends.clear()
                }
                currentDesc = line
            }
        }
    }
    if (currentDesc.isNotEmpty()) {
        entries.add(SummaryEntry(currentDesc, currentTrends.toList()))
    }
    return entries
}

private fun formatDate(dateStr: String): String {
    return try {
        val year = dateStr.substring(0, 4)
        val month = dateStr.substring(4, 6)
        val day = dateStr.substring(6, 8)
        "$day/$month/$year"
    } catch (e: Exception) {
        dateStr
    }
}