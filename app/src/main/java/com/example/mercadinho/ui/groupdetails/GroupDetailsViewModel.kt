package com.example.mercadinho.ui.groupdetails

import android.os.Bundle
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.UserInfo
import com.example.mercadinho.ui.groupdetails.GroupDetailsActivity.Companion.GROUP
import com.example.mercadinho.util.BaseViewModel

class GroupDetailsViewModel : BaseViewModel<GroupDetailsIntent, GroupDetailsState>() {

    lateinit var shopGroup: ShopGroup

    override val initialState: GroupDetailsState
        get() = GroupDetailsState.InitialState

    override fun handle(intent: GroupDetailsIntent) {
        when (intent) {
            is GroupDetailsIntent.InitArgs -> getArgs(intent.args)
            is GroupDetailsIntent.EditGroupTitle -> editGroupTitle(intent.title)
            is GroupDetailsIntent.GiveAdmin -> giveAdmin(intent.user)
            is GroupDetailsIntent.RemoveUser -> removeUser(intent.user)
            is GroupDetailsIntent.EditGroupDescription -> editGroupDescription(intent.description)
        }
    }

    private fun editGroupDescription(description: String) {

    }

    private fun editGroupTitle(title: String) {

    }

    private fun removeUser(user: UserInfo) {

    }

    private fun giveAdmin(user: UserInfo) {

    }

    private fun getArgs(args: Bundle) {
        shopGroup = args.get(GROUP) as ShopGroup
        _state.value = GroupDetailsState.ShowDetails(shopGroup)
    }
}