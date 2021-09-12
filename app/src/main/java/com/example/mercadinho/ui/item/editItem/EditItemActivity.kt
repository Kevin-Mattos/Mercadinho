package com.example.mercadinho.ui.item.editItem

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mercadinho.R
import com.example.mercadinho.databinding.ActivityEditItemBinding
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.ui.item.editItem.EditItemActivity.Companion.ITEM_KEY
import com.example.mercadinho.util.setResultAndFinish
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EditItemActivity : AppCompatActivity() {

    val viewModel: EditItemViewModel by viewModels()
    val binding by lazy {
        ActivityEditItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        getArgs()

        setListeners()
        setObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_item_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.remove_item -> {
                viewModel.handle(EditItemIntent.DeleteItem)
                true
            }
            R.id.edit_item -> {
                viewModel.handle(EditItemIntent.EditItem(getItemName()))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getItemName(): String = binding.itemNameET.text.toString()

    private fun setListeners() = binding.run {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setObservers() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect {
            when (it) {
                EditItemState.Edited -> onEdited()
                EditItemState.InitialState -> {
                }
                EditItemState.Removed -> onRemoved()
                is EditItemState.DisplayItem -> displayItem(it.shopItem)
            }
        }
    }

    private fun displayItem(shopItem: ShopItem) = binding.run {
        itemNameET.setText(shopItem.name)
    }

    private fun onRemoved() {
        setResultAndFinish()
    }

    private fun onEdited() {
        setResultAndFinish()
    }

    private fun getArgs() {
        intent.extras?.let {
            viewModel.handle(EditItemIntent.InitArgs(it))
        } ?: finish()
    }

    companion object {
        const val ITEM_KEY = "ITEM_KEY"
    }
}

fun Context.createEditItemIntent(item: ShopItem): Intent {
    return Intent(this, EditItemActivity::class.java).apply {
        putExtra(ITEM_KEY, item)
    }
}