package com.example.mercadinho.ui.item.editItem

import android.os.Bundle
import com.example.mercadinho.repository.EditItemRepository
import com.example.mercadinho.repository.ShopItemRepository
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.ui.item.editItem.EditItemActivity.Companion.ITEM_KEY
import com.example.mercadinho.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(val repository: EditItemRepository) :
    BaseViewModel<EditItemIntent, EditItemState>() {

    override val initialState: EditItemState
        get() = EditItemState.InitialState

    lateinit var item: ShopItem

    override fun handle(intent: EditItemIntent) {
        when (intent) {
            EditItemIntent.DeleteItem -> deleteItem()
            is EditItemIntent.EditItem -> editItem(intent.itemName)
            is EditItemIntent.InitArgs -> getArgs(intent.args)
        }
    }

    private fun getArgs(args: Bundle) {
        item = args.get(ITEM_KEY) as ShopItem
        _state.value = EditItemState.DisplayItem(item)
    }

    private fun editItem(itemName: String) {
        val newItem = item.copy(name = itemName)
        newItem.id = item.id
        repository.updateItem(newItem) {
            item = newItem
            _state.value = EditItemState.Edited
        }
    }

    private fun deleteItem() {
        repository.removeItem(item) {
            _state.value = EditItemState.Removed
        }
    }
}