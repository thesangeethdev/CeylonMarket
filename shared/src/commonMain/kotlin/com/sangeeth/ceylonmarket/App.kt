package com.sangeeth.ceylonmarket

import androidx.compose.runtime.*

import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel
import ui.theme.CeylonMarketTheme
import ui.theme.screens.DetailScreen
import ui.theme.screens.HomeScreen
import viewmodel.DetailViewModel
import viewmodel.HomeViewModel

@Composable
fun App() {
    PreComposeApp {
        CeylonMarketTheme {
            val navigator = rememberNavigator()

            NavHost(
                navigator = navigator,
                initialRoute = "/home"
            ) {
                scene("/home") {
                    val viewModel = viewModel(modelClass = HomeViewModel::class) { HomeViewModel() }
                    HomeScreen(
                        viewModel = viewModel,
                        onReportClick = { date ->
                            navigator.navigate("/detail/$date")
                        }
                    )
                }
                scene("/detail/{date}") { backStackEntry ->
                    val date = backStackEntry.path<String>("date") ?: ""
                    println("🔴 Navigation: date from backStackEntry.path = '$date'")
                    println("🔴 Navigation: full arguments = ${backStackEntry}")

                    val detailViewModel = viewModel(DetailViewModel::class){ DetailViewModel() }
                    detailViewModel.setDate(date)
                    DetailScreen(
                        viewModel = detailViewModel,
                        onBack = { navigator.goBack() }
                    )
                }
            }
        }
    }
}