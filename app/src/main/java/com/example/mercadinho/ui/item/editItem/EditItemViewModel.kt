package com.example.mercadinho.ui.item.editItem

import android.os.Bundle
import com.example.mercadinho.repository.ITEMS_KEY
import com.example.mercadinho.repository.ShopItemRepository
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.util.BaseViewModel
import javax.inject.Inject

class EditItemViewModel @Inject constructor(val repository: ShopItemRepository): BaseViewModel<EditItemIntent, EditItemState>() {

    override val initialState: EditItemState
        get() = EditItemState.InitialState

    var item: ShopItem? = null

    override fun handle(intent: EditItemIntent) {
        when(intent) {
            EditItemIntent.DeleteItem -> deleteItem()
            is EditItemIntent.EditItem -> editItem()
            is EditItemIntent.InitArgs -> getArgs(intent.args)
        }
    }

    private fun getArgs(args: Bundle) {
        item = args.get(ITEMS_KEY) as ShopItem?
    }

    private fun editItem() {

    }

    private fun deleteItem() {

    }
}