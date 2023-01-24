package com.merc.mercadao.ui.item.editItem

import com.merc.mercadao.repository.entities.ShopItem

sealed class EditItemState {
    object InitialState : EditItemState()
    object Edited: EditItemState()
    object Removed: EditItemState()
    data class DisplayItem(val shopItem: ShopItem): EditItemState()
}
