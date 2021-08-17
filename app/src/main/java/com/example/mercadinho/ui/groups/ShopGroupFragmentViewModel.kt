package com.example.mercadinho.viewmodels

import com.example.mercadinho.repository.ShopGroupRepository
import com.example.mercadinho.repository.add
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.teste.Grupos
import com.example.mercadinho.repository.remove
import com.example.mercadinho.util.BaseViewModel
import com.example.mercadinho.util.singleSubscribe
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

sealed class ShopGroupListFragmentState {
    data class GetAllGroups(val groupList: List<ShopGroup>) : ShopGroupListFragmentState()
    data class OnAddedError(val message: String, val code: AddErrorCodes) :
        ShopGroupListFragmentState() {
        enum class AddErrorCodes(val code: Int) {
            INVALID_NAME(1)
        }
    }
    data class ShareGroup(val group: ShopGroup) : ShopGroupListFragmentState()
}

sealed class ShopGroupListFragmentIntent {
    object GetAllGroups : ShopGroupListFragmentIntent()
    data class OnAdded(val shopGroup: ShopGroup) : ShopGroupListFragmentIntent()
    data class RemoveGroup(val group: ShopGroup) : ShopGroupListFragmentIntent()
    data class SearchGroup(val query: String) : ShopGroupListFragmentIntent()
    data class LeaveGroup(val group: ShopGroup) : ShopGroupListFragmentIntent()
    data class JoinGroup(val groupId: String) : ShopGroupListFragmentIntent()
    data class OnClickShare(val group: ShopGroup) : ShopGroupListFragmentIntent()
}

@HiltViewModel
class ShopGroupFragmentViewModel @Inject constructor(val shopRepository: ShopGroupRepository) :
    BaseViewModel<ShopGroupListFragmentIntent, ShopGroupListFragmentState>() {

    private val TAG: String = "ShopGroupFragmentViewModel"
    override val initialState: ShopGroupListFragmentState =
        ShopGroupListFragmentState.GetAllGroups(emptyList())
    val groups: MutableStateFlow<MutableList<Grupos>> =
        MutableStateFlow(mutableListOf())

    init {
        shopRepository.initStuff(
            onGroupAdded = ::onGroupAdded,
            onGroupChanged = ::onGroupChanged,
            onGroupRemoved = ::onGroupRemoved
        )
    }

    override fun handle(intent: ShopGroupListFragmentIntent) {
        when (intent) {
            is ShopGroupListFragmentIntent.OnAdded -> insertShopGroup(intent.shopGroup)
            is ShopGroupListFragmentIntent.RemoveGroup -> removeGroup(intent.group)
            is ShopGroupListFragmentIntent.SearchGroup -> queryGroups(intent.query)
            is ShopGroupListFragmentIntent.JoinGroup -> joinGroup(intent.groupId)
            is ShopGroupListFragmentIntent.LeaveGroup -> leaveGroup(intent.group)
            is ShopGroupListFragmentIntent.OnClickShare -> shareGroup(intent.group)
        }
    }

    private fun queryGroups(query: String) {
        disposable.add(
            Single.create<List<Grupos>> {
                val filteredGroups = groups.value.filter { it.groupName.contains(query) }
                it.onSuccess(filteredGroups)
            }.singleSubscribe(
                onSuccess = {
                    _state.value = ShopGroupListFragmentState.GetAllGroups(it.toShopGroup())
                }
            )
        )
    }

    private fun removeGroup(group: ShopGroup) {
        shopRepository.removeGroupFB(group)
    }

    private fun insertShopGroup(shopGroup: ShopGroup) {
        try {
            shopGroup.validate()
            shopRepository.addGroupFB(shopGroup)
        } catch (e: RuntimeException) {
            _state.value = ShopGroupListFragmentState.OnAddedError(
                e.message ?: "Unexpected failure",
                ShopGroupListFragmentState.OnAddedError.AddErrorCodes.INVALID_NAME
            )
        }
    }

    private fun joinGroup(groupId: String) {
        shopRepository.joinGroup(groupId)
    }

    private fun leaveGroup(group: ShopGroup) = shopRepository.leaveGroup(group)

    private fun shareGroup(group: ShopGroup) {
        _state.value = ShopGroupListFragmentState.ShareGroup(group)
    }

    private fun onGroupAdded(group: Grupos) {
        groups.add(group)
        showGroups()
    }

    private fun onGroupChanged(group: Grupos) {
        val group2 = groups.value.first { group.groupId == it.groupId }
        group2.groupName = group.groupName
        showGroups()
    }

    private fun onGroupRemoved(group: String?) {
        groups.remove(group)
        showGroups()
    }

    private fun showGroups() {
        _state.value = ShopGroupListFragmentState.GetAllGroups(groups.value.toShopGroup())
    }
}

fun List<Grupos>.toShopGroup(): List<ShopGroup> {
    return this.map { map -> ShopGroup(map.groupName).apply { id = map.groupId } }
}