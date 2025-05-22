package com.rach.lawyerapp.ui.home.data.dataSource

import com.rach.lawyerapp.ui.home.data.model.TransactionModel

object TransactionDataSource {
    val transactionData = listOf(
        TransactionModel(
            title = "A",
            addedOrDeduct = "Added",
            date = "12 Jan",
            amount = 499
        ),
        TransactionModel(
            title = "D",
            addedOrDeduct = "Deducted",
            date = "12 Jan",
            amount = 40
        ),
        TransactionModel(
            title = "A",
            addedOrDeduct = "Addeded",
            date = "12 Jan",
            amount = 999
        ), TransactionModel(
            title = "A",
            addedOrDeduct = "Added",
            date = "12 Jan",
            amount = 99
        ),
        TransactionModel(
            title = "D",
            addedOrDeduct = "Deducted",
            date = "18 Jan",
            amount = 99
        )
    )
}