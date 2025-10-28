package com.example.mybank.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybank.data.model.Account
import com.example.mybank.databinding.ActivityMainBinding
import com.example.mybank.databinding.DialogAddBinding
import com.example.mybank.ui.viewModel.AccountViewModel
import com.example.mybank.ui.adapter.AccountsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AccountsAdapter

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeLivedata()
        initAdapter()
        binding.btnAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun subscribeLivedata() {
        viewModel.accounts.observe(this) {
            adapter.submitList(it)
            //adapter.submitList(accountList)
            //отображаем то что придет с презентора
        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddDialog() {
        val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
        with(binding) {
            AlertDialog.Builder(this@MainActivity).setTitle("Добавление нового счета")
                .setView(binding.root).setPositiveButton("Добавить") { _, _ ->
                    val account = Account(
                        name = etName.text.toString(),
                        balance = etBalance.text.toString().toInt(),
                        currency = etCurrency.text.toString()
                    )
                    viewModel.addAccount(account)

                }.show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccounts()
    }

    private fun initAdapter() = with(binding) {
        adapter = AccountsAdapter(
            onSwitchToggle = { id, isCheked ->
                viewModel.updateAccountPartially(id, isCheked)
            },
            onItemClick = {
                val intent = Intent(this@MainActivity, AccountDetailsActivity::class.java)
                intent.putExtra("account", it)
                startActivity(intent)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }


}