package com.example.mybank.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybank.R
import com.example.mybank.data.model.Account
import com.example.mybank.databinding.ActivityMainBinding
import com.example.mybank.domain.presenter.AccountContracts
import com.example.mybank.domain.presenter.AccountPresenter
import com.example.mybank.ui.adapter.AccountsAdapter

class MainActivity : AppCompatActivity(), AccountContracts.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AccountsAdapter
    private lateinit var presenter: AccountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        presenter = AccountPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadAccounts()
    }
    private fun initAdapter() = with(binding) {
        adapter = AccountsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    override fun showAccounts(accountList: List<Account>) {
        adapter.submitList(accountList)
        //отображаем то что придет с презентора
    }
}