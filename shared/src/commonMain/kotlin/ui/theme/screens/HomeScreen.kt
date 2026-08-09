package ui.theme.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import data.model.PriceReport
import ui.theme.AmberSecondary
import ui.theme.ErrorRed
import ui.theme.OnSurface
import ui.theme.OnSurfaceVariant
import ui.theme.Surface
import ui.theme.TealPrimary
import viewmodel.HomeUiState
import viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onReportClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CeylonMarket", color = Surface) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TealPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.loadData() },
                containerColor = TealPrimary,
                contentColor = Surface
            ) {
                Icon(Icons.Default.Refresh, "Refresh")
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            when (uiState) {
                is HomeUiState.Loading -> LoadingScreen()
                is HomeUiState.Error -> ErrorScreen(
                    (uiState as HomeUiState.Error).message,
                    onRetry = { viewModel.loadData() }
                )

                is HomeUiState.Success -> {
                    val state = uiState as HomeUiState.Success
                    HomeContent(
                        latest = state.latest,
                        recentReports = state.recentReports,
                        onReportClick = onReportClick
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    latest: PriceReport,
    recentReports: List<String>,
    onReportClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {

            LatestReportCard(latest, onClick = {
                onReportClick(latest.date)
            })
        }

        item {
            Text(
                "Recent Reports",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(recentReports) { reportName ->
            val date = reportName.removePrefix("price_report_").removeSuffix(".json")
            ReportListItem(date = date, onClick = { onReportClick(date) })
        }
    }
}

@Composable
private fun LatestReportCard(latest: PriceReport, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = TealPrimary
        )
    ){
        Column(
            modifier = Modifier.padding(20.dp)
        ){
            Text(
                "Latest Report",
                fontSize = 14.sp,
                color = Surface.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Tap to view details ->",
                fontSize = 14.sp,
                color = AmberSecondary
            )
        }
    }
}

@Composable
private fun ReportListItem(date: String, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                formatDate(date),
                fontSize = 16.sp,
                color = OnSurface
            )
            Text(
                "→",
                fontSize = 20.sp,
                color = TealPrimary
            )
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