package com.example.mercadinho.repository

import androidx.lifecycle.LiveData
import com.example.mercadinho.repository.database.shop.ShopDatabaseManager
import com.example.mercadinho.repository.entities.ShopGroup
import kotlinx.coroutines.*

class ShopRepository(private val dbManager: ShopDatabaseManager) {

    fun getAllShops(): LiveData<List<ShopGroup>> = dbManager.getAllShops()

    fun insertShopGroup(shopGroup: ShopGroup) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            dbManager.insertShopGroup(shopGroup)
        }
    }

}