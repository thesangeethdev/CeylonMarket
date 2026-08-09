package com.sangeeth.ceylonmarket

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import ui.theme.CeylonMarketTheme
import ui.theme.screens.DetailScreen
import ui.theme.screens.HomeScreen
import viewmodel.DetailViewModel
import viewmodel.HomeViewModel

@Composable
@Preview
fun App() {
//    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
//    }
    PreComposeApp {
        CeylonMarketTheme {
            val navigator = rememberNavigator()

            NavHost(
                navigator = navigator,
                initialRoute = "/home"
            ) {
                scene("/home") {
                    val viewModel = viewModel(modelClass = HomeViewModel::class)
                    HomeScreen(
                        viewModel = viewModel,
                        onReportClick = { date ->
                            navigator.navigate("/detail/$date")
                        }
                    )
                }
                scene("/detail/{date}") { backStackEntry ->
                    val date = backStackEntry.path<String>("date") ?: ""
                    val detailViewModel = viewModel(DetailViewModel::class)
                    DetailScreen(
                        viewModel = detailViewModel,
                        onBack = { navigator.goBack() }
                    )
                }
            }
        }
    }
}