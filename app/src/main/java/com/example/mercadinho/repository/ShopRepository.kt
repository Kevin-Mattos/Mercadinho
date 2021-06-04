package com.example.mercadinho.repository

import androidx.lifecycle.LiveData
import com.example.mercadinho.repository.database.shop.ShopDatabaseManager
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import kotlinx.coroutines.*

class ShopRepository(private val dbManager: ShopDatabaseManager) {

    fun getAllShops(): LiveData<List<ShopGroup>> = dbManager.getAllShops()

    fun insertShopGroup(shopGroup: ShopGroup) {
        CoroutineScope(Dispatchers.IO).launch {
            //delay(2000)
            dbManager.insertShopGroup(shopGroup)
        }
    }

    fun deleteAllGroups() {
        CoroutineScope(Dispatchers.IO).launch {
            dbManager.deleteAllGroups()
        }
    }

    fun insertShopItem(shopItem: ShopItem) {

        CoroutineScope(Dispatchers.IO).launch {
//            delay(2000)
            dbManager.insertShopItem(shopItem)

        }
    }

    fun getAllItems(): LiveData<List<ShopItem>> = dbManager.getAllItems()

    fun getItemByGroupId(groupId: Long) = dbManager.getItemByGroupId(groupId)

    fun updateAllShopItens(shopItems: List<ShopItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            dbManager.updateAllShopItens(shopItems)
        }
    }

}