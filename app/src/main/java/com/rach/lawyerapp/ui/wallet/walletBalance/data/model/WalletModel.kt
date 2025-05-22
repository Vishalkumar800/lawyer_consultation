package com.rach.lawyerapp.ui.wallet.walletBalance.data.model

data class WalletModel(
    val userId:String? = "TestingUser",
    val amount:Int = 0
)

data class WalletDataBaseModel(
    val userId: String? = "TestingUser",
    val amount: Int = 0
){
    fun toDomain() = WalletModel(
        userId = userId,
        amount = amount
    )
}