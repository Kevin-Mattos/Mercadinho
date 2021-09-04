package com.example.mercadinho.ui.groups

import com.example.mercadinho.repository.entities.ShopGroup

sealed class ShopGroupListFragmentIntent {
    object GetAllGroups : ShopGroupListFragmentIntent()
    data class OnAdded(val shopGroup: ShopGroup) : ShopGroupListFragmentIntent()
    data class RemoveGroup(val group: ShopGroup) : ShopGroupListFragmentIntent()
    data class SearchGroup(val query: String) : ShopGroupListFragmentIntent()
    data class JoinGroup(val groupId: String) : ShopGroupListFragmentIntent()
    data class OnClickShare(val group: ShopGroup) : ShopGroupListFragmentIntent()
}