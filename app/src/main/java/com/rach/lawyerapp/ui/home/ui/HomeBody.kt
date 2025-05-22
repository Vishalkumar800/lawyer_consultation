package com.rach.lawyerapp.ui.home.ui

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.lawyerapp.MainActivity
import com.rach.lawyerapp.R
import com.rach.lawyerapp.appPermissions.viewModel.PermissionViewModel
import com.rach.lawyerapp.ui.components.ErrorScreen
import com.rach.lawyerapp.ui.components.LoadingView
import com.rach.lawyerapp.ui.home.data.model.LawyerModel
import com.rach.lawyerapp.ui.home.ui.components.CategoryAndFilter
import com.rach.lawyerapp.ui.home.ui.components.LawyerCard
import com.rach.lawyerapp.ui.home.viewModel.LawyerUiEvents
import com.rach.lawyerapp.ui.home.viewModel.LawyerViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(
    onDetailsClick: (LawyerModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LawyerViewModel = hiltViewModel(),
    permissionViewModel: PermissionViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val allCategoryName = stringResource(R.string.all_chips)

    // Handle back press to reset to "All" when not already on "All" or in a dialog
    BackHandler(enabled = uiState.isNotFound || uiState.lawyers.isEmpty()) {
        viewModel.onEvents(LawyerUiEvents.OnFilterChange(listOf(allCategoryName)))
    }

    /*
    .
    .
    .
     for permissions
     .
     .
     .
     */

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true && permissions[Manifest.permission.POST_NOTIFICATIONS] == true &&
                permissions[Manifest.permission.RECORD_AUDIO] == true
            ) {
                //Permission Granted
            } else {
                val rationalRequired =
                    permissionViewModel.shouldShowPermissionRationaleGranted(context as MainActivity)

                if (rationalRequired) {
                    Toast.makeText(context, "Permission required to work", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        context,
                        "Permission Denied. Please enable in Settings",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }


    /*
    .
    .
    Home Ui Code
    .
    .
    */

    when {
        uiState.isLoading -> {
            LoadingView(modifier = modifier)
        }

        uiState.errorMessage != null -> {
            ErrorScreen(
                retry = { viewModel.onEvents(events = LawyerUiEvents.FetchLawyers) },
                errorMessage = R.string.failed_to_load,
                modifier = modifier
            )
        }

        uiState.isNotFound -> {
            NotFoundView(
                modifier = modifier
            )
        }

        else -> {
            HomeBody(
                lawyers = uiState.lawyers,
                onDetailsClick, modifier,
                viewModel = viewModel
            )
        }
    }
}


@Composable
fun NotFoundView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.cloud_off), // Add an appropriate drawable
            contentDescription = "Not Found",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No lawyers found",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try a different search term",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun HomeBody(
    lawyers: List<LawyerModel>,
    onDetailsClick: (LawyerModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LawyerViewModel
) {
    LazyColumn(
        modifier = modifier
    ) {

        item {
            CategoryAndFilter(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel
            )
        }
        items(lawyers) { data ->
            LawyerCard(
                onDetailsClick = { onDetailsClick(data) },
                lawyerModel = data,
                userId = data.id!!,
                userName = data.name
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_padding)))
        }

    }
}