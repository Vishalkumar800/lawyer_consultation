package com.rach.lawyerapp.dagger

import com.rach.lawyerapp.ui.home.data.repo.LawyerRepository
import com.rach.lawyerapp.ui.home.data.repoImp.LawyerRepositoryImp
import com.rach.lawyerapp.ui.wallet.walletBalance.data.repo.WalletRepository
import com.rach.lawyerapp.ui.wallet.walletBalance.data.repoImp.WalletRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsLawyerRepository(
        lawyerRepositoryImp: LawyerRepositoryImp
    ): LawyerRepository

    @Binds
    @Singleton
    abstract fun bindsWalletRepository(
        walletRepositoryImp: WalletRepositoryImp
    ): WalletRepository


}