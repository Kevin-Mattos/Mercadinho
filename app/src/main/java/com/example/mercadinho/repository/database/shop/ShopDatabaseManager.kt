package com.example.mercadinho.repository.database.shop

import androidx.lifecycle.LiveData
import com.example.mercadinho.repository.entities.ShopGroup

class ShopDatabaseManager(private val shopDao: ShopDao) {

    fun getAllShops(): LiveData<List<ShopGroup>> = shopDao.getAllGroups()

    fun insertShopGroup(shopGroup: ShopGroup) {
            shopDao.insertShopGroup(shopGroup)
    }
}