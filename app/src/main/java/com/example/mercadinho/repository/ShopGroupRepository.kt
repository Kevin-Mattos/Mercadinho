package com.example.mercadinho.repository

import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.repository.entities.UserInfo
import com.google.firebase.database.DatabaseError

interface ShopGroupRepository {
    fun initStuff(
        onGroupAdded: ((ShopGroup) -> Unit),
        onGroupChanged: ((ShopGroup) -> Unit),
        onGroupRemoved: ((String?) -> Unit)
    )
    fun addGroupFB(group: ShopGroup, userInfo: UserInfo)
    fun removeGroupFB(group: ShopGroup)
    fun joinGroup(groupId: String, userInfo: UserInfo, failedToJoin: (() -> Unit)? = null)
    fun leaveGroup(group: ShopGroup)
}

interface ShopGroupDetailsRepository {
    fun editGroupDescription(group: ShopGroup, description: String)
    fun editGroupName(group: ShopGroup, title: String)
    fun removeUser(group: ShopGroup, user: UserInfo)
    fun giveAdmin(group: ShopGroup, user: UserInfo)
    fun getGroup(group: ShopGroup,
                 onGroupDetailsUpdated: ((ShopGroup) -> Unit),
                 onGroupParticipantsUpdated: ((List<UserInfo>) -> Unit))
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