package com.example.mercadinho.ui.item.editItem

import com.example.mercadinho.repository.entities.ShopItem

sealed class EditItemState {
    object InitialState : EditItemState()
    object Edited: EditItemState()
    object Removed: EditItemState()
    data class DisplayItem(val shopItem: ShopItem): EditItemState()
}
