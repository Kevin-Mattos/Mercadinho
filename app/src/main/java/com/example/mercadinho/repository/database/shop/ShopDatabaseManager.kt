package com.example.mercadinho.repository.database.shop

import androidx.lifecycle.LiveData
import com.example.mercadinho.repository.entities.ShopGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShopDatabaseManager(private val shopDao: ShopDao) {

    fun getAllShops(): LiveData<List<ShopGroup>> = shopDao.getAll()

    fun insertShopGroup(shopGroup: ShopGroup) {
            shopDao.insertShopGroup(shopGroup)
    }
}