package com.example.mercadinho.ui.item.editItem

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mercadinho.databinding.ActivityEditItemBinding
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.ui.item.ShopItemListFragmentState
import com.example.mercadinho.ui.item.editItem.EditItemActivity.Companion.ITEM_KEY
import kotlinx.coroutines.flow.collect

class EditItemActivity: AppCompatActivity() {

    val viewModel by viewModels<EditItemViewModel>()
    val binding by lazy {
        ActivityEditItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
        setObservers()
    }

    private fun setObservers() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect {
            when (it) {
                EditItemState.Edited -> onEdited()
                EditItemState.InitialState -> { }
                EditItemState.Removed -> onRemoved()
            }
        }
    }

    private fun onRemoved() {
        finish()
    }

    private fun onEdited() {
        finish()
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