package com.example.mercadinho.repository

import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.repository.entities.teste.Grupos
import com.google.firebase.database.DatabaseError

interface ShopGroupRepository {
    fun initStuff(
        onGroupAdded: ((Grupos) -> Unit),
        onGroupChanged: ((Grupos) -> Unit),
        onGroupRemoved: ((String?) -> Unit)
    )
    fun addGroupFB(group: ShopGroup)
    fun removeGroupFB(group: ShopGroup)
    fun joinGroup(groupId: String, failedToJoin: (() -> Unit)? = null)
    fun leaveGroup(group: ShopGroup)
}

interface ShopItemRepository {
    fun getItemByShopId(
        itemId: String, onUpdate: ((Map<String, Any>?) -> Unit)? = null,
        onCanceled: ((DatabaseError) -> Unit)? = null
    )
    fun addItem(item: ShopItem)
    fun removeItemFB(item: ShopItem)
    fun updateItemFB(item: ShopItem)
}