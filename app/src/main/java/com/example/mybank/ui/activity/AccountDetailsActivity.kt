package com.example.mybank.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybank.data.model.Account
import com.example.mybank.databinding.ActivityAccountDetailsBinding
import com.example.mybank.databinding.DialogAddBinding
import com.example.mybank.ui.adapter.AccountsAdapter
import com.example.mybank.ui.viewModel.AccountDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountDetailsActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityAccountDetailsBinding
    private val viewModel: AccountDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val account = intent.getSerializableExtra("account") as Account
        subscribeLivedata()
        with(binding) {
            btnEdit.setOnClickListener {
                showEditDialog(account)
            }
            btnDelete.setOnClickListener {
                account.id?.let { id -> showDeleteDialog(id)

                }
            }
        }

        with(binding) {
            tvInfo.text = "Id счета: ${account.id}"
            tvName.text = "Название счета: ${account.name}"
            tvCurrency.text = "Валюта: ${account.currency}"
            tvBalance.text = "Баланс: ${account.balance.toString()}"
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.accounts.value?.id?.let {
            viewModel.accounts.value?.let { account ->
                viewModel.getAccountById(
                    it,
                    account
                )
            }
        }
    }


    private fun subscribeLivedata() {
        viewModel.accounts.observe(this) {
            viewModel.accounts.value?.id?.let { id ->
                viewModel.accounts.value?.let { account ->
                    viewModel.getAccountById(
                        id,
                        account
                    )
                }
            }

        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(
                this,
                "Проверьте подключение к интернету",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun showDeleteDialog(id: String) {
        AlertDialog.Builder(this).setTitle("Вы уверены?")
            .setMessage("Удалить счет №${id}?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteAccount(id)
                finish()
            }.setNegativeButton("Отмена") { _, _ ->

            }.show()
    }

    private fun showEditDialog(account: Account) {
        val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
        with(binding) {
            account.run {
                //run позволяет использовать значения account.
                // но его отличия от apply  в том что он не возвращает его обратно.
                // он возвращает последнее значение

                etName.setText(name)
                etBalance.setText(balance.toString())
                etCurrency.setText(currency)


                AlertDialog.Builder(this@AccountDetailsActivity).setTitle("Изменение счета")
                    .setView(binding.root).setPositiveButton("Изменить") { _, _ ->
                        val updateAccount = account.copy(
                            //он копирует модельку. все значения он меняет, а ID и isActive оставляет тот же
                            name = etName.text.toString(),
                            balance = etBalance.text.toString().toInt(),
                            currency = etCurrency.text.toString()
                        )

                        viewModel.updateAccountFully(updateAccount)
                        finish()
                    }.show()


            }
        }
    }
}

