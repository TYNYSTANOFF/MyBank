package com.example.mybank.domain.presenter

import com.example.mybank.data.model.Account


class AccountPresenter(private val view: AccountContracts.View) : AccountContracts.Presenter {
    override fun loadAccounts() {
        //здесь обработка счетов
        val testMockList = arrayListOf<Account>()
        testMockList.add(
            Account(
                name = "O!Bank",
                balance = 1000,
                currency ="KGS"
        ))
        testMockList.add(
            Account(
                name = "MBank",
                balance = 100,
                currency ="USD"
        ))
        testMockList.add(
            Account(
                name = "OptimaBank",
                balance = 100,
                currency ="EUR"
        ))
        view.showAccounts(testMockList)
    }
}