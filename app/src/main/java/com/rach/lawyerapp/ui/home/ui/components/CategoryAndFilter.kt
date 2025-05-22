package com.rach.lawyerapp.ui.home.ui.components

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.home.viewModel.LawyerUiEvents
import com.rach.lawyerapp.ui.home.viewModel.LawyerViewModel
import com.rach.lawyerapp.ui.theme.LawyerAppTheme

data class FilterCategory(
    @StringRes val name: Int,
    @DrawableRes val icon: Int,
    val showMoreCategoryOptions: Boolean = false
)

private val initialCategory = listOf(
    FilterCategory(
        name = R.string.filter_chips,
        icon = R.drawable.sort,
        showMoreCategoryOptions = true
    ),
    FilterCategory(name = R.string.all_chips, icon = R.drawable.all),
    FilterCategory(name = R.string.love_chips, icon = R.drawable.love),
    FilterCategory(name = R.string.divorce, icon = R.drawable.divorce),
)

private val additionalCategory = listOf(
    FilterCategory(name = R.string.criminal_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.environmental_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.family_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.corporate_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.civil_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.intellectual_property_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.tax_lawyer, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.cyber_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.estate_planning_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.worker_compensation_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.public_interest_lawyers, icon = R.drawable.baseline_account_balance_wallet_24),
    FilterCategory(name = R.string.merger_and_acquisition, icon = R.drawable.baseline_account_balance_wallet_24)
)


@Composable
fun CategoryAndFilter(
    viewModel: LawyerViewModel,
    modifier: Modifier = Modifier
) {
    var selectedChipIndex by remember { mutableIntStateOf(1) }
    var showCategoryMoreOptions by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val allCategoryName = stringResource(id = initialCategory[1].name)

    // Handle back
    BackHandler(enabled = selectedChipIndex != 1 || showCategoryMoreOptions) {
        if (showCategoryMoreOptions) {
            showCategoryMoreOptions = false // Close dialog first
        } else if (selectedChipIndex != 1) {
            selectedChipIndex = 1 // Reset to "All"
            viewModel.onEvents(events = LawyerUiEvents.OnFilterChange(listOf(allCategoryName)))
        }
    }

    Row(modifier = modifier.horizontalScroll(state = scrollState)) {
        initialCategory.forEachIndexed { index, filterCategory ->
            val categoryName = stringResource(id = filterCategory.name) // Resolve string here
            SignalChipsDesign(
                selected = selectedChipIndex == index,
                onChipsClick = {
                    selectedChipIndex = index
                    if (filterCategory.showMoreCategoryOptions) {
                        showCategoryMoreOptions = true
                    } else {
                        viewModel.onEvents(events = LawyerUiEvents.OnFilterChange(listOf(categoryName)))
                    }
                },
                icon = filterCategory.icon,
                categoryName = filterCategory.name
            )
        }

        if (showCategoryMoreOptions) {
            ShowMoreCategoryOptions(
                onDismissRequest = { showCategoryMoreOptions = false },
                selectedCategory = emptySet(),
                categories = additionalCategory,
                onSelectedCategory = { selectedCategories ->
                    viewModel.onEvents(LawyerUiEvents.OnFilterChange(selectedCategories.toList()))
                    showCategoryMoreOptions = false
                    selectedChipIndex = 1
                }
            )
        }
    }
}

@Composable
private fun ShowMoreCategoryOptions(
    onDismissRequest: () -> Unit,
    selectedCategory: Set<String>, // Stores resolved string values
    categories: List<FilterCategory>,
    onSelectedCategory: (Set<String>) -> Unit,
    maxSelection: Int = 10,
    modifier: Modifier = Modifier
) {
    var tempSelectedCategory by remember { mutableStateOf(selectedCategory) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.filter_chips)) },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(categories) { item ->
                    val categoryName = stringResource(item.name)
                    val isLongCategory = categoryName.length > 15

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isLongCategory) Arrangement.Start else Arrangement.SpaceBetween
                    ) {
                        FilterChip(
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.six_dp))
                                .then(if (isLongCategory) Modifier.fillMaxWidth() else Modifier.weight(1f)),
                            selected = tempSelectedCategory.contains(categoryName),
                            onClick = {
                                val newSelection = tempSelectedCategory.toMutableSet()
                                if (newSelection.contains(categoryName)) {
                                    newSelection.remove(categoryName)
                                } else {
                                    if (newSelection.size < maxSelection) {
                                        newSelection.add(categoryName)
                                    }
                                }
                                tempSelectedCategory = newSelection
                            },
                            label = { Text(text = categoryName) },
                            shape = MaterialTheme.shapes.extraLarge,
                        )

                        // Agar category chhoti hai, tabhi doosra chip dikhayenge
                        if (!isLongCategory) {
                            val nextIndex = categories.indexOf(item) + 1
                            if (nextIndex < categories.size) {
                                val nextItem = categories[nextIndex]
                                val nextCategoryName = stringResource(nextItem.name)
                                val isNextLong = nextCategoryName.length > 15

                                if (!isNextLong) {
                                    FilterChip(
                                        modifier = Modifier
                                            .padding(dimensionResource(R.dimen.six_dp))
                                            .weight(1f),
                                        selected = tempSelectedCategory.contains(nextCategoryName),
                                        onClick = {
                                            val newSelection = tempSelectedCategory.toMutableSet()
                                            if (newSelection.contains(nextCategoryName)) {
                                                newSelection.remove(nextCategoryName)
                                            } else {
                                                if (newSelection.size < maxSelection) {
                                                    newSelection.add(nextCategoryName)
                                                }
                                            }
                                            tempSelectedCategory = newSelection
                                        },
                                        label = { Text(text = nextCategoryName) },
                                        shape = MaterialTheme.shapes.extraLarge,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSelectedCategory(tempSelectedCategory)
            }) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}


@Composable
fun SignalChipsDesign(
    selected: Boolean = false,
    onChipsClick: () -> Unit, // Reverted to non-Composable lambda
    @DrawableRes icon: Int,
    @StringRes categoryName: Int,
    enable: Boolean = true,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier.padding(6.dp),
        selected = selected,
        onClick = onChipsClick, // Directly use the non-Composable lambda
        label = { Text(text = stringResource(categoryName)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        },
        shape = MaterialTheme.shapes.large,
        enabled = enable
    )
}

@AppPreview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LawyerAppTheme {
        SignalChipsDesign(
            modifier = modifier,
            selected = false,
            onChipsClick = {}, // Non-Composable lambda
            icon = R.drawable.love,
            categoryName = R.string.love_chips
        )
    }
}