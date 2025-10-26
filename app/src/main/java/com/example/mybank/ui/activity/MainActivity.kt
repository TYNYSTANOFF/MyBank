package com.example.mybank.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
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
        initAdapter()
        subscribeLivedata()
        binding.btnAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun subscribeLivedata(){
        viewModel.accounts.observe(this){
            adapter.submitList(it)
            //adapter.submitList(accountList)
            //отображаем то что придет с презентора
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
            onEdit = {
                showEditDialog(it)
            },
            onSwitchToggle = { id, isCheked ->
                viewModel.updateAccountPartially(id, isCheked)
            },
            onDelete = {
                // если одно значение можно не писать id ->.
                // а если больше то расписывать
                showDeleteDialog(it)

            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    private fun showDeleteDialog(id: String) {
        AlertDialog.Builder(this).setTitle("Вы уверены?")
            .setMessage("Удалить счет №${id}?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteAccount(id)
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


                AlertDialog.Builder(this@MainActivity).setTitle("Изменение счета")
                    .setView(binding.root).setPositiveButton("Изменить") { _, _ ->

                        val updateAccount = account.copy(
                            //он копирует модельку. все значения он меняет, а ID и isActive оставляет тот же

                            name = etName.text.toString(),
                            balance = etBalance.text.toString().toInt(),
                            currency = etCurrency.text.toString()
                        )


                        viewModel.updateAccountFully(updateAccount)

                    }.show()
            }
        }

    }

}