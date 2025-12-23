package com.qadri.to_do.ui.homescreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.FrameMetricsAggregator.ANIMATION_DURATION
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qadri.to_do.R
import com.qadri.to_do.model.Task
import com.qadri.to_do.ui.ToDoAppTopBar
import com.qadri.to_do.ui.navigation.NavigationDestination
import com.qadri.to_do.ui.utils.ExpandAndShrinkAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.factory),
    navigateToAddItem: () -> Unit,
    navigateToUpdateItem: (Int) -> Unit
) {
    val allTaskList = viewModel.getAllTasks.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ToDoAppTopBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                editScreen = true,
                scrollBehavior = scrollBehavior,
                deleteAll = {
                    deleteConfirmationRequired = true
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddItem,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_tasks)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "New Task", fontSize = 17.sp)
                }
            }
        }
    ) {
        HomeBody(
            allTask = allTaskList.value,
            onTaskClick = navigateToUpdateItem,
            onCheckedChange = { task ->
                viewModel.updateIsCompleted(task)
            },
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            onDelete = { task ->
                coroutineScope.launch {
                    viewModel.deleteTask(task)
                }
            }
        )
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    coroutineScope.launch {
                        viewModel.deleteAllTasks()
                    }
                },
                onDeleteCompleted = {
                    coroutineScope.launch {
                        deleteConfirmationRequired = false
                        viewModel.deleteCompleted()
                    }
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun HomeBody(
    allTask: List<Task>,
    onTaskClick: (Int) -> Unit,
    onCheckedChange: (Task) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (Task) -> Unit
) {
    Column(
        modifier = modifier.padding(top = 8.dp)
    ) {
        if (allTask.isEmpty()) {
            Box(modifier = modifier.fillMaxSize()) {
                Text(
                    text = stringResource(id = R.string.no_item_desc),
                    modifier = Modifier.align(Center)
                )
            }
        } else {
            TaskList(
                allTasks = allTask,
                onCheckedChange = { onCheckedChange(it) },
                onTaskClick = { onTaskClick(it.id) },
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun TaskList(
    allTasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    onCheckedChange: (Task) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (Task) -> Unit
) {
    val (completedTasks, incompletedTasks) = allTasks.partition { it.isCompleted }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        items(incompletedTasks, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onCheckedChange = {
                    onCheckedChange(task)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onTaskClick(task)
                    },
//                isCompleted = false,
                onDelete = {
                    onDelete(task)
                }
            )
        }

        items(completedTasks, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onCheckedChange = {
                    onCheckedChange(task)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onTaskClick(task)
                    },
//                isCompleted = true,
                onDelete = {
                    onDelete(task)
                }
            )
        }
    }
}


@Composable
fun TaskItem(
    modifier: Modifier,
    task: Task,
    onCheckedChange: () -> Unit,
//    isCompleted: Boolean,
    onDelete: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    // Add our dismiss state for the animation
    val dismissState = rememberSwipeToDismissBoxState()
    // Swipe from left to right to dismiss
    val isDismissed = dismissState.progress == 1f

    val itemAppeared = remember { mutableStateOf(false) }

    LaunchedEffect(
        key1 = true,
    ) {
        itemAppeared.value = true
    }

    itemAppeared.ExpandAndShrinkAnimation {
        SwipeToDismissBox(
            state = dismissState,
            modifier = Modifier,
            enableDismissFromEndToStart = false,
            onDismiss = {
                if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                    coroutineScope.launch {
                        itemAppeared.value = false
                        delay(ANIMATION_DURATION.toLong())
                        onDelete()
                    }
                }
            },
            backgroundContent = {
                val color by animateColorAsState(
                    when (dismissState.targetValue) {
                        SwipeToDismissBoxValue.Settled -> Color.White
                        else -> MaterialTheme.colorScheme.errorContainer
                    },
                    label = ""
                )
                val scale by animateFloatAsState(
                    if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
                    label = ""
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = color, shape = MaterialTheme.shapes.medium),
                    contentAlignment = CenterStart
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Icon",
                        modifier = Modifier.scale(scale),
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.background
                )
            ) {
                TaskCard(
                    task = task,
                    onCheckedChange = { onCheckedChange() },
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task, onCheckedChange: () -> Unit, modifier: Modifier,
//    isCompleted: Boolean
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            Column(
                modifier = Modifier.weight(5f)
            ) {
                Text(
                    text = task.taskName,
                    color = MaterialTheme.colorScheme.scrim,
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )

                Text(
                    text = task.taskDescription,
                    color = MaterialTheme.colorScheme.scrim,
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_small),
                        bottom = dimensionResource(id = R.dimen.padding_small)
                    )
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onCheckedChange() },
                    modifier = Modifier.align(Center),
                    colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCompleted: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.scrim)
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteCompleted) {
                Text(
                    text = stringResource(id = R.string.delete_selected),
                    color = MaterialTheme.colorScheme.scrim
                )
            }
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.delete_all), color = MaterialTheme.colorScheme.scrim)
            }
        }
    )
}

@Preview
@Composable
fun TaskCardPreview() {
    TaskCard(
        task = Task(1, "name", "desc", true),
        onCheckedChange = { /*TODO*/ },
        modifier = Modifier
    )
}