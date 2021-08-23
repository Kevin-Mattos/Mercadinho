package com.example.mercadinho.repository

import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.google.firebase.database.DatabaseError

interface ShopGroupRepository {
    fun initStuff(
        onGroupAdded: ((ShopGroup) -> Unit),
        onGroupChanged: ((ShopGroup) -> Unit),
        onGroupRemoved: ((String?) -> Unit)
    )
    fun addGroupFB(group: ShopGroup)
    fun removeGroupFB(group: ShopGroup)
    fun joinGroup(groupId: String, failedToJoin: (() -> Unit)? = null)
    fun leaveGroup(group: ShopGroup)
}

interface ShopGroupDetailsRepository {

}

interface ShopItemRepository {
    fun getItemByShopId(
        itemId: String, onUpdate: ((List<ShopItem>) -> Unit)? = null,
        onCanceled: ((DatabaseError) -> Unit)? = null
    )
    fun addItem(item: ShopItem)
    fun removeItemFB(item: ShopItem)
    fun updateItem(item: ShopItem)
}