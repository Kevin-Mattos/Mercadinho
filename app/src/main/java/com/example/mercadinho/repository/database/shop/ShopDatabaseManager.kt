package com.example.mercadinho.repository.database.shop

import androidx.lifecycle.LiveData
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem

class ShopDatabaseManager(private val shopDao: ShopDao) {

    fun getAllShops(): LiveData<List<ShopGroup>> = shopDao.getAllGroups()

    fun insertShopGroup(shopGroup: ShopGroup) {
            shopDao.insertShopGroup(shopGroup)
    }

    fun deleteAllGroups() {
        shopDao.deleteAllGroups()
    }

    fun insertShopItem(shopItem: ShopItem) {
        shopDao.insertShopItem(shopItem)
    }

    fun getAllItems(): LiveData<List<ShopItem>> = shopDao.getAllItems()

    fun getItemByGroupId(groupId: Long): LiveData<List<ShopItem>> = shopDao.getAllItemsFromGroup(groupId)
}