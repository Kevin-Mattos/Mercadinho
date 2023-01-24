package com.colodori.mercadinho.ui.groupdetails

import android.os.Bundle
import com.colodori.mercadinho.repository.entities.UserInfo

sealed class GroupDetailsIntent {
    data class InitArgs(val args: Bundle): GroupDetailsIntent()
    data class EditGroupTitle(val title: String): GroupDetailsIntent()
    data class EditGroupDescription(val description: String): GroupDetailsIntent()
    data class RemoveUser(val user: UserInfo): GroupDetailsIntent()
    data class GiveAdmin(val user: UserInfo): GroupDetailsIntent()
    object IdClicked: GroupDetailsIntent()
    object LeaveGroup: GroupDetailsIntent()

}