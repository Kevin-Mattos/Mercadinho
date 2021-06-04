package com.example.mercadinho.ui.main

import androidx.lifecycle.ViewModel
import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.entities.ShopGroup
import org.koin.java.KoinJavaComponent.inject

class MainViewModel : ViewModel() {

    private val shopRepository by inject(ShopRepository::class.java)

    fun insertShopGroup(shopGroup: ShopGroup) {
        shopRepository.insertShopGroup(shopGroup)
    }

    fun getAllGroups() = shopRepository.getAllShops()
}