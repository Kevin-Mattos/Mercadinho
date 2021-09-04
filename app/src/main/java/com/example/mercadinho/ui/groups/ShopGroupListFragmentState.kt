package com.example.mercadinho.ui.groups

import androidx.annotation.StringRes
import com.example.mercadinho.repository.entities.ShopGroup

sealed class ShopGroupListFragmentState {
    data class GetAllGroups(val groupList: List<ShopGroup>) : ShopGroupListFragmentState()
    data class OnAddedError(val message: String, val code: AddErrorCodes) :
        ShopGroupListFragmentState() {
        enum class AddErrorCodes(val code: Int) {
            INVALID_NAME(1)
        }
    }
    data class ShareGroup(val group: ShopGroup) : ShopGroupListFragmentState()
    data class FailedToJoin(@StringRes val reasonId: Int) : ShopGroupListFragmentState()
}