@file:OptIn(ExperimentalMaterial3Api::class)

package com.qadri.to_do.ui.add_edit_task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.qadri.to_do.R
import com.qadri.to_do.model.Task
import com.qadri.to_do.model.TaskUiState
import com.qadri.to_do.ui.ToDoAppTopBar
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object TaskInputDestination

@Composable
fun TaskInputScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TaskEditViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ToDoAppTopBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                editScreen = false
            )
        }
    ) {
        TaskInputBody(
            modifier = Modifier.padding(it),
            taskUiState = viewModel.taskUiState,
            onTaskValueChange = { task ->
                viewModel.updateUiState(task)
            },
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTask()
                    navigateBack()
                }
            }
        )
    }
}

@Composable
fun TaskInputBody(
    taskUiState: TaskUiState,
    onTaskValueChange: (Task) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        TaskInputForm(
            taskDetails = taskUiState.task,
            onTaskValueChange = { onTaskValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        Button(
            onClick = onSaveClick,
            enabled = taskUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@Composable
fun TaskInputForm(
    taskDetails: Task,
    modifier: Modifier = Modifier,
    onTaskValueChange: (Task) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier) {
        OutlinedTextField(
            value = taskDetails.taskName,
            onValueChange = { onTaskValueChange(taskDetails.copy(taskName = it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(id = R.string.task_name))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        OutlinedTextField(
            value = taskDetails.taskDescription,
            onValueChange = { onTaskValueChange(taskDetails.copy(taskDescription = it)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(top = dimensionResource(id = R.dimen.padding_small)),
            placeholder = {
                Text(text = stringResource(R.string.task_desc))
            }
        )
    }
}