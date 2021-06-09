package com.example.mercadinho.repository

import androidx.lifecycle.LiveData
import com.example.mercadinho.repository.database.shop.ShopDatabaseManager
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import kotlinx.coroutines.*

class ShopRepository(private val dbManager: ShopDatabaseManager) {

    fun getAllShops(callBack: (LiveData<List<ShopGroup>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val groups = dbManager.getAllShops()
            withContext(Dispatchers.Main) {
                callBack(groups)
            }
        }
    }

    fun insertShopGroup(shopGroup: ShopGroup) {
        CoroutineScope(Dispatchers.IO).launch {
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
            dbManager.insertShopItem(shopItem)
        }
    }

    fun getAllItems(callBack: (LiveData<List<ShopItem>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val itens = dbManager.getAllItems()
            withContext(Dispatchers.Main) {
                callBack(itens)
            }
        }
    }

    fun getItemByGroupId(groupId: Long, callBack: (LiveData<List<ShopItem>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val itens = dbManager.getItemByGroupId(groupId)
            withContext(Dispatchers.Main) {
                callBack(itens)
            }
        }
    }

    fun updateAllShopItens(shopItems: List<ShopItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            dbManager.updateAllShopItens(shopItems)
        }
    }

    fun removeItem(shopItem: ShopItem) {
        CoroutineScope(Dispatchers.IO).launch {
            dbManager.deleteItem(shopItem)
        }
    }

    fun removeGroup(group: ShopGroup) {
        CoroutineScope(Dispatchers.IO).launch {
            dbManager.removeGroup(group)
        }
    }
}