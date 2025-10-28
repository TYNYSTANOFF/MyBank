package com.example.mybank.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybank.data.model.Account
import com.example.mybank.data.model.AccountState
import com.example.mybank.data.network.AccountDetailApi
import com.example.mybank.data.network.AccountsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    private val accountDetailApi: AccountDetailApi
) : ViewModel() {
    private val _accounts = MutableLiveData<Account>()
    private val _errorMessage = MutableLiveData<String>()
    val accounts: LiveData<Account> = _accounts
    val errorMessage: LiveData<String> = _errorMessage
    fun getAccountById(id: String, account: Account) {
        accountDetailApi.getAccountsById(id, account).handleAccountResponce()
    }

    fun updateAccountFully(updateAccount: Account) {
        updateAccount.id?.let {
            accountDetailApi.updateAccountFully(it, updateAccount).handleAccountResponce()
        }

    }

    fun deleteAccount(id: String) {
        accountDetailApi.deleteAccount(id).handleAccountResponce()//возвратить на прошлый активити
    }


    //ЗДЕСЬ не уверен
    private fun <T> Call<T>.handleAccountResponce(
        onSuccess: (T) -> Unit = {
            accounts.value?.id?.let { id ->
                accounts.value?.let { account ->
                    getAccountById(
                        id,
                        account
                    )
                }
            }
        },//показать элемент
        onError: (String) -> Unit = { _errorMessage.postValue(it) }
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







