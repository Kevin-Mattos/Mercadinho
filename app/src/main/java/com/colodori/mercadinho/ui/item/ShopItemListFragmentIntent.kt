package com.colodori.mercadinho.ui.item

import com.colodori.mercadinho.repository.entities.ShopItem

sealed class ShopItemListFragmentIntent {
    object GetAllItensById : ShopItemListFragmentIntent()
    data class OnQuery(val query: String) : ShopItemListFragmentIntent()
    data class OnAdded(val shopItem: ShopItem) : ShopItemListFragmentIntent()
    data class RemoveItem(val shopItem: ShopItem) : ShopItemListFragmentIntent()
    data class UpdateItem(val item: ShopItem) : ShopItemListFragmentIntent()
}