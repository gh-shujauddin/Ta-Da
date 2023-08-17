package com.qadri.to_do.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.qadri.to_do.ui.homescreen.HomeDestination
import com.qadri.to_do.ui.homescreen.HomeScreen
import com.qadri.to_do.ui.homescreen.TaskEditDestination
import com.qadri.to_do.ui.homescreen.TaskEditScreen
import com.qadri.to_do.ui.homescreen.TaskInputDestination
import com.qadri.to_do.ui.homescreen.TaskInputScreen

@Composable
fun ToDoNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAddItem = { navController.navigate(TaskInputDestination.route) },
                navigateToUpdateItem = {
                    navController.navigate("${TaskEditDestination.route}/${it}")
                }
            )
        }
        composable(route = TaskInputDestination.route) {
            TaskInputScreen(
                navigateBack = { navController.navigateUp() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = TaskEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskEditDestination.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskEditScreen(
                onNavigateUp = { navController.popBackStack() },
                navigateBack = { navController.navigateUp() })
        }
    }
}