package com.example.mybank.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybank.data.model.Account
import com.example.mybank.data.model.AccountState
import com.example.mybank.data.network.AccountsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountsApi : AccountsApi
): ViewModel() {
    private val _accounts = MutableLiveData<List<Account>>()
    private val _errorMessage = MutableLiveData<String>()
    val accounts: LiveData<List<Account>> = _accounts
     val errorMessage : LiveData<String> = _errorMessage
    fun loadAccounts() {
        accountsApi.getAccounts().handleAccountResponce(
            onSuccess = { _accounts.value = it })
    }

    fun addAccount(account: Account) {
        accountsApi.addAccount(account).handleAccountResponce()
    }

    fun updateAccountFully(updateAccount: Account) {
        updateAccount.id?.let {
            accountsApi.updateAccountFully(it, updateAccount).handleAccountResponce()
        }
    }

    fun updateAccountPartially(id: String, isCheked: Boolean) {
        accountsApi.updateAccountPartially(id, AccountState(isCheked))
            .handleAccountResponce()
    }

    fun deleteAccount(id: String) {
       accountsApi.deleteAccount(id).handleAccountResponce()
    }

    private fun <T> Call<T>.handleAccountResponce(
        onSuccess: (T) -> Unit = { loadAccounts() },
        //если оставить то по умолчанию будет loadAccounts(),
        // но можно повторно вызвать и перезаписать например как onSuccess = { _accounts.value = it }
        onError: (String) -> Unit = { _errorMessage.postValue(it)}
    ) {
        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                val result = response.body()
                if (result != null && response.isSuccessful) {
                    onSuccess(result)
                } else {
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                onError(t.message.toString())
            }

        })
    }
}