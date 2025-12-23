package com.qadri.to_do.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
        startDestination = HomeDestination,
        modifier = modifier
    ) {
        composable<HomeDestination> {
            HomeScreen(
                navigateToAddItem = { navController.navigate(TaskInputDestination) },
                navigateToUpdateItem = {
                    navController.navigate(TaskEditDestination(it))
                }
            )
        }
        composable<TaskInputDestination> {
            TaskInputScreen(
                navigateBack = { navController.navigateUp() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable<TaskEditDestination> { backStackEntry ->
            val args = backStackEntry.toRoute<TaskEditDestination>()
            TaskEditScreen(
                onNavigateUp = { navController.popBackStack() },
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}