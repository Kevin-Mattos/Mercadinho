package com.example.mercadinho.ui.item

import com.example.mercadinho.repository.entities.ShopItem

sealed class ShopItemListFragmentState {
    data class GetAllItensById(val shopItemList: List<ShopItem>) : ShopItemListFragmentState()
}
