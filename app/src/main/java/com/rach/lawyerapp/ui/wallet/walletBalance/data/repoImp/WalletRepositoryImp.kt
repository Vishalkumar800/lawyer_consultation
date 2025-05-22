package com.rach.lawyerapp.ui.wallet.walletBalance.data.repoImp

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rach.lawyerapp.ui.wallet.walletBalance.data.model.WalletDataBaseModel
import com.rach.lawyerapp.ui.wallet.walletBalance.data.model.WalletModel
import com.rach.lawyerapp.ui.wallet.walletBalance.data.repo.WalletRepository
import com.rach.lawyerapp.utils.DataBaseConstants
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WalletRepositoryImp @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : WalletRepository {

    companion object {
        const val DEFAULT_USER_ID = "TestingUser"
    }


    private fun getWalletReference() =
        firebaseDatabase.getReference(DataBaseConstants.WALLET_COLLECTION)
            .child(DEFAULT_USER_ID)

    override suspend fun addMoney(amount: Int) {
        if (amount <= 0) {
            throw DataBaseException("Amount must be positive")
        }
        val userId = DEFAULT_USER_ID
        val reference = getWalletReference()

        try {
            val snapshot = reference.get().await()
            val currentWallet = snapshot.getValue(WalletDataBaseModel::class.java)
            val newAmount = currentWallet?.amount?.plus(amount) ?: amount
            Log.d("RazorPayVM", "New amount calculated: $newAmount")

            reference.setValue(WalletDataBaseModel(userId, newAmount)).await()
            Log.d("RazorPayVM", "Money added successfully to database")
        } catch (e: Exception) {
            Log.e("RazorPayVM", "Error in addMoney: ${e.message}", e)
            throw DataBaseException(e.message ?: "Unknown Error While Adding Money")
        }
    }

    override fun deductMoney(amount: Int) {
        try {
            if (amount <= 0) {
                throw DataBaseException("Amount must be positive")
            }

            val userId = DEFAULT_USER_ID
            val reference = getWalletReference()
            reference.get().addOnSuccessListener { snapshot ->
                val currentWallet = snapshot.getValue(WalletDataBaseModel::class.java)
                val currentAmount = currentWallet?.amount ?: 0
                if (currentAmount < amount) {
                    throw DataBaseException("Insufficient balance")
                }

                val newAmount = currentAmount - amount
                reference.setValue(WalletDataBaseModel(userId, newAmount))
                    .addOnFailureListener { e ->
                        throw DataBaseException("Failed To Deduct Money ${e.localizedMessage}")
                    }
            }.addOnFailureListener { e ->
                throw DataBaseException("Failed To Fetch current Balance ${e.localizedMessage}")
            }

        } catch (e: Exception) {
            throw DataBaseException("Unknown Error While Deducting Money")
        }
    }

    override fun getWalletBalance(): Flow<Response<WalletModel>> = callbackFlow {
        val reference = getWalletReference()
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val walletData = snapshot.getValue(WalletDataBaseModel::class.java)
                    val walletModel = walletData?.toDomain() ?: WalletModel()
                    trySend(Response.Success(walletModel)).isSuccess // Check if send was successful
                } catch (e: Exception) {
                    trySend(Response.Error(DataBaseException("Failed to parse wallet data: ${e.message}")))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Response.Error(DataBaseException("Database error: ${error.message}")))
            }
        }

        reference.addValueEventListener(listener)
        // Cleanup when the flow is closed or cancelled
        awaitClose {
            reference.removeEventListener(listener)
        }
    }


}

class DataBaseException(message: String) : Exception(message)