package com.rach.lawyerapp.ui.wallet.walletBalance.data.repo

import com.rach.lawyerapp.ui.wallet.walletBalance.data.model.WalletModel
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface WalletRepository {

    suspend fun addMoney(amount: Int)
    fun deductMoney(amount: Int)
    fun getWalletBalance(): Flow<Response<WalletModel>>

}