package com.example.mercadinho.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import org.koin.java.KoinJavaComponent

class ShopItemFragmentViewModel : ViewModel() {

    private val shopRepository by KoinJavaComponent.inject(ShopRepository::class.java)

    var groupId = 0L

    fun insertShopItem(shopItem: ShopItem) = shopRepository.insertShopItem(shopItem)

    fun getItemById() = shopRepository.getItemByGroupId(groupId)

    fun updateAll(shopItems: List<ShopItem>) = shopRepository.updateAllShopItens(shopItems)

}