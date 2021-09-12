package com.example.mercadinho.repository

import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.repository.entities.UserInfo
import com.google.firebase.database.DatabaseError

interface ShopGroupRepository {
    /**
     * Initializes the observers for the groups and notifies when a group the user is in is changed
     *
     * @param onGroupAdded
     * @param onGroupChanged
     * @param onGroupRemoved
     */
    fun initStuff(
        onGroupAdded: ((ShopGroup) -> Unit),
        onGroupChanged: ((ShopGroup) -> Unit),
        onGroupRemoved: ((String?) -> Unit)
    )

    /**
     * Creates new group
     *
     * @param group
     * @param userInfo
     */
    fun addGroupFB(group: ShopGroup, userInfo: UserInfo)

    /**
     * Removes Group
     * User leaves grooup on groupDetails now
     * @param group
     */
    @Deprecated("Shouldnt be used anymore")
    fun removeGroupFB(group: ShopGroup)

    /**
     * User joins a group that already exists
     * callback failedToJoin is called in case group doesnt exist
     *
     * @param groupId
     * @param userInfo
     * @param failedToJoin
     */
    fun joinGroup(groupId: String, userInfo: UserInfo, failedToJoin: (() -> Unit)? = null)
}

interface ShopGroupDetailsRepository {
    /**
     * Edits the description of the group
     *
     * @param group
     * @param description
     */
    fun editGroupDescription(group: ShopGroup, description: String)

    /**
     * Edits the name of the group
     * FUTURE RELEASE
     *
     * @param group
     * @param title
     */
    fun editGroupName(group: ShopGroup, title: String)

    /**
     * Removes user from a group
     * User has to be admin
     * FUTURE RELEASE
     *
     * @param group
     * @param user
     */
    fun removeUser(group: ShopGroup, user: UserInfo)

    /**
     * Gives another user admin status
     * FUTURE RELEASE
     *
     * @param group
     * @param user
     */
    fun giveAdmin(group: ShopGroup, user: UserInfo)

    /**
     * observes the group and notifies changed
     *
     * @param group
     * @param onGroupDetailsUpdated
     * @param onGroupParticipantsUpdated
     */
    fun getGroup(group: ShopGroup,
                 onGroupDetailsUpdated: ((ShopGroup) -> Unit),
                 onGroupParticipantsUpdated: ((List<UserInfo>) -> Unit))

    /**
     * Leaves the selected group
     *
     * @param group
     */
    fun leaveGroup(group: ShopGroup)
}

interface ShopItemRepository {
    /**
     * Gets the itens in a group and notifies changes
     *
     * @param itemId
     * @param onUpdate
     * @param onCanceled
     */
    fun getItemByShopId(
        itemId: String, onUpdate: ((List<ShopItem>) -> Unit)? = null,
        onCanceled: ((DatabaseError) -> Unit)? = null
    )

    /**
     * adds another item on the list
     *
     * @param item
     */
    fun addItem(item: ShopItem)

    /**
     * Removes an item fom the list
     * Remove is in another place now
     * will this remove function later.
     * @param item
     */
    @Deprecated("Shouldnt be used anymore")
    fun removeItemFB(item: ShopItem)

    /**
     * updates an item (bought status)
     *
     * @param item
     */
    fun updateItem(item: ShopItem)
}

interface EditItemRepository {
    /**
     * Updates an item (only its name for now)
     *
     * @param item
     * @param onUpdated
     */
    fun updateItem(item: ShopItem, onUpdated: (() -> Unit)? = null)

    /**
     * removes an item from the list
     *
     * @param item
     * @param onRemoved
     */
    fun removeItem(item: ShopItem, onRemoved: (() -> Unit)? = null)
}