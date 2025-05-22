package com.rach.lawyerapp.admin.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rach.lawyerapp.AppPreview
import com.rach.lawyerapp.ui.theme.LawyerAppTheme

@Composable
fun IndianStatesDropdown() {
    var selectedState by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val indianStates = listOf(
        "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat",
        "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
        "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
        "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh",
        "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
        "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Jammu and Kashmir", "Ladakh",
        "Lakshadweep", "Puducherry"
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = selectedState,
            onValueChange = { /* Read-only, no direct input */ },
            label = { Text("Select State") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { expanded = !expanded }, // Open dropdown on field click
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    } // Open dropdown on icon click
                )
            },
            readOnly = true // Prevents keyboard input
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            indianStates.forEach { state ->
                DropdownMenuItem(
                    text = { Text(state) },
                    onClick = {
                        selectedState = state
                        expanded = false
                    },
                )
            }
        }
    }
}

@AppPreview
@Composable
private fun Preview() {
    LawyerAppTheme {
        IndianStatesDropdown()
    }
}