package com.example.mybank.data.network

import com.example.mybank.data.model.Account
import com.example.mybank.data.model.AccountState
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountDetailApi {

    @PUT("accounts/{id}")
    fun updateAccountFully(
        @Path("id") id: String,
        @Body updateAccount: Account
    ): Call<Unit>


    @DELETE("accounts/{id}")
    fun deleteAccount(
        @Path("id") id: String
    ): Call<Unit>

    @GET("accounts/{id}")
    fun getAccountsById(
        @Path("id") id: String,
        @Body account: Account
    ): Call<Unit>


}