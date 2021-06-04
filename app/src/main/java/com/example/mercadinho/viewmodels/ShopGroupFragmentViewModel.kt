package com.example.mercadinho.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import org.koin.java.KoinJavaComponent.inject

class ShopGroupFragmentViewModel : ViewModel() {

    private val shopRepository by inject(ShopRepository::class.java)

    fun insertShopGroup(shopGroup: ShopGroup) {
        shopRepository.insertShopGroup(shopGroup)
    }

    fun getAllGroups() = shopRepository.getAllShops()

    fun deleteAllGroups() = shopRepository.deleteAllGroups()

    fun insertShopItem(shopItem: ShopItem) = shopRepository.insertShopItem(shopItem)

    fun getAllItems() = shopRepository.getAllItems()
}