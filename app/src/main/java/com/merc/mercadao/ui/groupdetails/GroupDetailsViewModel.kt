package com.merc.mercadao.ui.groupdetails

import android.os.Bundle
import com.merc.mercadao.repository.ShopGroupDetailsRepository
import com.merc.mercadao.repository.entities.ShopGroup
import com.merc.mercadao.repository.entities.UserInfo
import com.merc.mercadao.ui.groupdetails.GroupDetailsActivity.Companion.GROUP
import com.merc.mercadao.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class GroupDetailsViewModel @Inject constructor(private val repository: ShopGroupDetailsRepository)
    : BaseViewModel<GroupDetailsIntent, GroupDetailsState>() {

    private lateinit var shopGroup: ShopGroup

    override val initialState: GroupDetailsState
        get() = GroupDetailsState.InitialState

    override fun handle(intent: GroupDetailsIntent) {
        when (intent) {
            is GroupDetailsIntent.InitArgs -> getArgs(intent.args)
            is GroupDetailsIntent.EditGroupTitle -> editGroupTitle(intent.title)
            is GroupDetailsIntent.GiveAdmin -> giveAdmin(intent.user)
            is GroupDetailsIntent.RemoveUser -> removeUser(intent.user)
            is GroupDetailsIntent.EditGroupDescription -> editGroupDescription(intent.description)
            is GroupDetailsIntent.IdClicked -> idCLicked()
            GroupDetailsIntent.LeaveGroup -> leaveGroup()
        }
    }

    private fun leaveGroup() {
        repository.leaveGroup(shopGroup)
        _state.value = GroupDetailsState.LeaveGroup
    }

    private fun idCLicked() {
        _state.value = GroupDetailsState.CopyId(shopGroup.id)
    }

    private fun editGroupDescription(description: String) {
        repository.editGroupDescription(shopGroup, description)
    }

    private fun editGroupTitle(title: String) {
        repository.editGroupName(shopGroup, title)
    }

    private fun removeUser(user: UserInfo) {
        repository.removeUser(shopGroup, user)
    }

    private fun giveAdmin(user: UserInfo) {
        repository.giveAdmin(shopGroup, user)
    }

    private fun showGroupDetails(group: ShopGroup) {
        val id = shopGroup.id
        shopGroup = group.also { it.id = id }

        _state.value = GroupDetailsState.ShowDetails(shopGroup)
    }

    private fun showParticipants(participants: List<UserInfo>) {
        _state.value = GroupDetailsState.ShowParticipants(participants)
    }

    private fun getArgs(args: Bundle) {
        shopGroup = args.get(GROUP) as ShopGroup
        _state.value = GroupDetailsState.ShowDetails(shopGroup)
        repository.getGroup(shopGroup, ::showGroupDetails, ::showParticipants)
    }
}