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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                    ReportContent(report)
                }
            }
        }
    }
}

@Composable
private fun ReportContent(report: PriceReport) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary
        report.data.summary?.let { summary ->
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = TealPrimary.copy(alpha = 0.08f)
                    )
                ) {
                    Text(
                        summary,
                        modifier = Modifier.padding(16.dp),
                        color = OnSurface,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Vegetables
        report.data.vegetables?.let { items ->
            item { SectionHeader("Vegetables") }
            items(items) { PriceRow(it) }
        }

        // Rice
        report.data.rice?.let { items ->
            item { SectionHeader("Rice") }
            items(items) { PriceRow(it) }
        }

        // Fish
        report.data.fish?.let { items ->
            item { SectionHeader("Fish") }
            items(items) { PriceRow(it) }
        }

        // Other
        report.data.other?.let { items ->
            item { SectionHeader("Other") }
            items(items) { PriceRow(it) }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = TealPrimary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun PriceRow(item: PriceItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
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
                        data.model.Trend.UP -> "↑"
                        data.model.Trend.DOWN -> "↓"
                        data.model.Trend.STABLE -> "→"
                    }
                    Text(
                        " $arrow",
                        color = color,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = TealPrimary)
    }
}

@Composable
private fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
        Spacer(modifier = Modifier.height(8.dp))
        Text(message, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)) {
            Text("Retry")
        }
    }
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