@file:OptIn(ExperimentalMaterial3Api::class)

package com.qadri.to_do.ui.homescreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qadri.to_do.R
import com.qadri.to_do.ui.ToDoAppTopBar
import com.qadri.to_do.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object TaskEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_task_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@Composable
fun TaskEditScreen(
    onNavigateUp: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEditViewModel = viewModel(factory = TaskEditViewModel.factory)
) {
    val coroutineScope = rememberCoroutineScope()
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ToDoAppTopBar(
                title = stringResource(id = TaskEditDestination.titleRes),
                canNavigateBack = true,
                editScreen = true,
                navigateUp = onNavigateUp,
                deleteAll = {
                    deleteConfirmationRequired = true
                }
            )
        }
    ) { innerPadding ->
        TaskInputBody(
            modifier = modifier.padding(innerPadding),
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = {
                viewModel.updateUiState(it)
            },
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTask()
                    navigateBack()
                }
            }
        )
        if (deleteConfirmationRequired) {
            DeleteTaskConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    coroutineScope.launch {
                        viewModel.deleteTask()
                    }
                    navigateBack()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun DeleteTaskConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.deleted_question_2)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no), color = MaterialTheme.colorScheme.scrim)
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes), color = MaterialTheme.colorScheme.scrim)
            }
        }
    )
}