package com.rach.lawyerapp.ui.wallet.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.lawyerapp.ui.wallet.walletBalance.data.repoImp.DataBaseException
import com.rach.lawyerapp.ui.wallet.walletBalance.useCases.AddMoneyUseCases
import com.rach.lawyerapp.ui.wallet.walletBalance.useCases.DeductMoneyUseCases
import com.rach.lawyerapp.ui.wallet.walletBalance.useCases.WalletBalanceUseCases
import com.rach.lawyerapp.utils.K
import com.rach.lawyerapp.utils.Response
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RazorPayViewModel @Inject constructor(
    private val addMoneyUseCases: AddMoneyUseCases,
    private val deductMoneyUseCases: DeductMoneyUseCases,
    private val getBalanceUseCases: WalletBalanceUseCases
) : ViewModel(), PaymentResultListener {

    private val _walletUiState = MutableStateFlow(WalletUiState())
    val walletUiState: StateFlow<WalletUiState> = _walletUiState.asStateFlow()

    private var lastPaymentAmount: Int = 0

    init {
        getWalletBalance()
    }

    fun onEvents(events: WalletUiEvents) {
        when (events) {
            is WalletUiEvents.OnUserEnterAmount -> {
                _walletUiState.value = _walletUiState.value.copy(
                    userEnterAmount = events.userEnterAmount,
                    successMessage = null,
                    errorMessage = null
                )
            }
        }
    }

    fun startPayment(activity: Activity) {
        val amount = _walletUiState.value.userEnterAmount.toIntOrNull() ?: 0
        if (amount <= 0) {
            _walletUiState.value = _walletUiState.value.copy(
                errorMessage = "Amount must be greater than zero",
                isLoading = false
            )
            return
        }

        lastPaymentAmount = amount

        _walletUiState.value = _walletUiState.value.copy(
            isLoading = true,
            successMessage = null,
            errorMessage = null
        )
        try {
            val options = JSONObject().apply {
                put("name", "Lawyer Consultation")
                put("description", "Add Money")
                put("theme", JSONObject().put("color", "#3399cc"))
                put("currency", "INR")
                put("amount", (amount * 100).toLong())
                val paymentMethods = JSONObject().apply {
                    put("upi", true)
                    put("qr", true)
                }
                put("method", paymentMethods)
                val upiConfig = JSONObject().apply {
                    put("flow", "intent")
                }
                put("upi", upiConfig)
            }

            val checkout = Checkout()
            checkout.setKeyID(K.RAZORPAY_ID)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("RazorPayVM", "Payment initiation failed: ${e.message}", e)
            _walletUiState.value = _walletUiState.value.copy(
                isLoading = false,
                errorMessage = "Payment initiation failed: ${e.localizedMessage}"
            )
        }

    }

    override fun onPaymentSuccess(razorpayId: String?) {
        Log.d("RazorPayVM", "Payment successful, Razorpay ID: $razorpayId")
        viewModelScope.launch {
            try {
                Log.d("RazorPayVM", "Calling addMoneyUseCases with amount: $lastPaymentAmount")
                addMoneyUseCases(lastPaymentAmount)
                Log.d("RazorPayVM", "addMoneyUseCases completed, fetching balance")
                getWalletBalance()
                Log.d("RazorPayVM", "Balance updated, setting UI state")
                _walletUiState.value = _walletUiState.value.copy(
                    isLoading = false,
                    successMessage = "Payment of $lastPaymentAmount added successfully",
                    errorMessage = null
                )
            } catch (e: Exception) {
                Log.e("RazorPayVM", "Error in onPaymentSuccess: ${e.message}", e)
                _walletUiState.value = _walletUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to update wallet: ${e.message}",
                    successMessage = null
                )
            }
        }
    }


    override fun onPaymentError(errorCode: Int, response: String?) {
        val errorMessage = when (errorCode) {
            Checkout.PAYMENT_CANCELED -> "Payment cancelled by user"
            Checkout.NETWORK_ERROR -> "Network error during payment"
            Checkout.INVALID_OPTIONS -> "Invalid payment options"
            else -> "Payment failed: $response (Code: $errorCode)"
        }
        _walletUiState.value = _walletUiState.value.copy(
            isLoading = false,
            errorMessage = errorMessage,
            successMessage = null
        )
    }


    fun deductMoneyFromWallet(amount: Int) {
        if (amount <= 0) {
            _walletUiState.value = _walletUiState.value.copy(
                errorMessage = "Amount to deduct must be greater than zero",
                isLoading = false
            )
            return
        }
        _walletUiState.value = _walletUiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                deductMoneyUseCases(amount)
                getWalletBalance()
                _walletUiState.value = _walletUiState.value.copy(
                    isLoading = false,
                    successMessage = "₹$amount deducted successfully",
                    errorMessage = null
                )
            } catch (e: DataBaseException) {
                Log.e("RazorPayVM", "Database error deducting money: ${e.message}", e)
                _walletUiState.value = _walletUiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to deduct money",
                    successMessage = null
                )
            } catch (e: Exception) {
                Log.e("RazorPayVM", "Unexpected error deducting money: ${e.message}", e)
                _walletUiState.value = _walletUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unexpected error: ${e.localizedMessage}",
                    successMessage = null
                )
            }
        }
    }

    private fun getWalletBalance() {
        viewModelScope.launch {
            try {
                getBalanceUseCases().collectLatest { response ->
                    when (response) {
                        is Response.Success -> {
                            _walletUiState.value = _walletUiState.value.copy(
                                balance = response.data!!.amount,
                                isLoading = false,
                                errorMessage = null
                            )
                        }

                        is Response.Error -> {
                            Log.e("RazorPayVM", "Wallet balance error: ${response.error?.message}")
                            _walletUiState.value = _walletUiState.value.copy(
                                isLoading = false,
                                errorMessage = response.error?.message
                                    ?: "Unknown error fetching balance"
                            )
                        }

                        is Response.Loading -> {
                            _walletUiState.value = _walletUiState.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("RazorPayVM", "Unexpected error getting balance: ${e.message}", e)
                _walletUiState.value = _walletUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to fetch balance: ${e.localizedMessage}"
                )
            }
        }
    }

    // Optional: Clear messages after they’ve been shown
    fun clearMessages() {
        _walletUiState.value = _walletUiState.value.copy(
            successMessage = null,
            errorMessage = null
        )
    }
}

data class WalletUiState(
    val userEnterAmount: String = "",
    val balance: Int = 0,
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

sealed class WalletUiEvents {
    data class OnUserEnterAmount(val userEnterAmount: String) : WalletUiEvents()
}