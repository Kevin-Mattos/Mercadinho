package com.colodori.mercadinho.ui.item.editItem

import com.colodori.mercadinho.repository.entities.ShopItem

sealed class EditItemState {
    object InitialState : EditItemState()
    object Edited: EditItemState()
    object Removed: EditItemState()
    data class DisplayItem(val shopItem: ShopItem): EditItemState()
}
