package com.example.mercadinho.repository

import com.example.mercadinho.repository.database.shop.ShopDatabaseManager
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ShopRepository @Inject constructor(private val dbManager: ShopDatabaseManager) {

    fun getAllShops() = dbManager.getAllShops()

    fun insertShopGroup(shopGroup: ShopGroup) = dbManager.insertShopGroup(shopGroup)

    fun deleteAllGroups() = dbManager.deleteAllGroups()

    fun insertShopItem(shopItem: ShopItem) = dbManager.insertShopItem(shopItem)

    fun getAllItems() = dbManager.getAllItems()

    fun getItemByGroupId(groupId: Long) = dbManager.getItemByGroupId(groupId)

    fun updateAllShopItens(shopItems: List<ShopItem>) = dbManager.updateAllShopItems(shopItems)

    fun removeItem(shopItem: ShopItem) = dbManager.deleteItem(shopItem)

    fun removeGroup(group: ShopGroup) = dbManager.removeGroup(group)

    fun getAllShopsRxJava(query: String): Single<List<ShopGroup>> = dbManager.getAllGroups2(query)

}