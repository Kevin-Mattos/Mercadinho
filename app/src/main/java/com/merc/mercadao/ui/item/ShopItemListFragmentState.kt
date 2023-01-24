package com.merc.mercadao.ui.item

import com.merc.mercadao.repository.entities.ShopItem

sealed class ShopItemListFragmentState {
    data class GetAllItensById(val shopItemList: List<ShopItem>) : ShopItemListFragmentState()
}
