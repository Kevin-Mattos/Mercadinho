package com.colodori.mercadinho.ui.item

import com.colodori.mercadinho.repository.entities.ShopItem

sealed class ShopItemListFragmentState {
    data class GetAllItensById(val shopItemList: List<ShopItem>) : ShopItemListFragmentState()
}
