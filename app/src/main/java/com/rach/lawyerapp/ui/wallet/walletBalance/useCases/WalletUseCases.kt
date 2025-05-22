package com.rach.lawyerapp.ui.wallet.walletBalance.useCases

import com.rach.lawyerapp.ui.wallet.walletBalance.data.model.WalletModel
import com.rach.lawyerapp.ui.wallet.walletBalance.data.repo.WalletRepository
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddMoneyUseCases @Inject constructor(
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(amount: Int) {
        walletRepository.addMoney(amount)
    }
}

class DeductMoneyUseCases @Inject constructor(
    private val walletRepository: WalletRepository
) {
    operator fun invoke(amount: Int) {
        walletRepository.deductMoney(amount)
    }
}

class WalletBalanceUseCases @Inject constructor(
    private val walletRepository: WalletRepository
) {
    operator fun invoke(): Flow<Response<WalletModel>> {
        return walletRepository.getWalletBalance()
    }
}