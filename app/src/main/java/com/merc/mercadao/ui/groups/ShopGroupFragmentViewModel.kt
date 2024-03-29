package com.merc.mercadao.ui.groups

import com.merc.mercadao.R
import com.merc.mercadao.repository.ShopGroupRepository
import com.merc.mercadao.repository.add
import com.merc.mercadao.repository.entities.ShopGroup
import com.merc.mercadao.repository.entities.UserInfo
import com.merc.mercadao.repository.remove
import com.merc.mercadao.util.BaseViewModel
import com.merc.mercadao.util.singleSubscribe
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow


@HiltViewModel
class ShopGroupFragmentViewModel @Inject constructor(val shopRepository: ShopGroupRepository) :
    BaseViewModel<ShopGroupListFragmentIntent, ShopGroupListFragmentState>() {

    private val TAG: String = "ShopGroupFragmentViewModel"
    override val initialState: ShopGroupListFragmentState =
        ShopGroupListFragmentState.GetAllGroups(emptyList())
    val groups: MutableStateFlow<MutableList<ShopGroup>> =
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
            is ShopGroupListFragmentIntent.OnClickShare -> shareGroup(intent.group)
            else -> {}
        }
    }

    private fun queryGroups(query: String) {
        disposable.add(
            Single.create<List<ShopGroup>> {
                val filteredGroups = groups.value.filter { it.name.contains(query) }
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
            val userInfo = UserInfo(isAdmin = true)
            shopRepository.addGroupFB(shopGroup, userInfo)
        } catch (e: RuntimeException) {
            _state.value = ShopGroupListFragmentState.OnAddedError(
                e.message ?: "Unexpected failure",
                ShopGroupListFragmentState.OnAddedError.AddErrorCodes.INVALID_NAME
            )
        }
    }

    private fun joinGroup(groupId: String) {
        val userInfo = UserInfo()
        shopRepository.joinGroup(groupId, userInfo) {
            _state.value = ShopGroupListFragmentState.FailedToJoin(R.string.failed_to_join)
        }
    }

    private fun shareGroup(group: ShopGroup) {
        _state.value = ShopGroupListFragmentState.ShareGroup(group)
    }

    private fun onGroupAdded(group: ShopGroup) {
        groups.add(group)
        showGroups()
    }

    private fun onGroupChanged(group: ShopGroup) {
        val group2 = groups.value.first { group.id == it.id }
        group2.name = group.name
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

fun List<ShopGroup>.toShopGroup(): List<ShopGroup> {
    return this.map { map -> ShopGroup(map.id, name = map.name) }
}