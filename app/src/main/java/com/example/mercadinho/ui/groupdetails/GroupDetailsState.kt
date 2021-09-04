package com.example.mercadinho.ui.groupdetails

import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.UserInfo

sealed class GroupDetailsState {
    object InitialState: GroupDetailsState()
    data class ShowDetails(val shopGroup: ShopGroup): GroupDetailsState()
    data class ShowParticipants(val participants: List<UserInfo>): GroupDetailsState()
    data class CopyId(val id: String): GroupDetailsState()
    object LeaveGroup: GroupDetailsState()
}