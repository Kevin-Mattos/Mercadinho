package com.example.mercadinho.viewmodels

import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.util.BaseViewModel
import com.example.mercadinho.util.completableSubscribe
import com.example.mercadinho.util.flowableSubscribe
import com.example.mercadinho.util.singleSubscribe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class ShopGroupListFragmentState {
    data class GetAllGroups(val groupList: List<ShopGroup>) : ShopGroupListFragmentState()
    data class OnAddedError(val message: String, val code: AddErrorCodes) :
        ShopGroupListFragmentState() {
        enum class AddErrorCodes(val code: Int) {
            INVALID_NAME(1)
        }
    }

    data class UpdateGroups(val groupList: List<ShopGroup>) : ShopGroupListFragmentState()
}

sealed class ShopGroupListFragmentIntent {
    object GetAllGroups : ShopGroupListFragmentIntent()
    data class OnAdded(val shopGroup: ShopGroup) : ShopGroupListFragmentIntent()
    data class RemoveGroup(val group: ShopGroup) : ShopGroupListFragmentIntent()
    data class SearchGroup(val query: String) : ShopGroupListFragmentIntent()
}
@HiltViewModel
class ShopGroupFragmentViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    BaseViewModel<ShopGroupListFragmentIntent, ShopGroupListFragmentState>() {




    override fun handle(intent: ShopGroupListFragmentIntent) {
        when (intent) {
            is ShopGroupListFragmentIntent.GetAllGroups -> getAllShopGroups()
            is ShopGroupListFragmentIntent.OnAdded -> insertShopGroup(intent.shopGroup)
            is ShopGroupListFragmentIntent.RemoveGroup -> removeGroup(intent.group)
            is ShopGroupListFragmentIntent.SearchGroup -> queryGroups(intent.query)
        }
    }

    private fun queryGroups(query: String) {
        disposable.add(
            shopRepository.getAllShopsRxJava(query).singleSubscribe(onSuccess = { list ->
                state.value = ShopGroupListFragmentState.UpdateGroups(list)
            })
        )
    }

    private fun removeGroup(group: ShopGroup) {
        disposable.add(shopRepository.removeGroup(group).completableSubscribe())
    }

    private fun getAllShopGroups() {
        disposable.add(
            shopRepository.getAllShops().flowableSubscribe(
                onNext = { groups ->
                    state.value = ShopGroupListFragmentState.GetAllGroups(groups)
                })
        )
    }

    private fun insertShopGroup(shopGroup: ShopGroup) {
        try {
            shopGroup.validate()
            disposable.add(shopRepository.insertShopGroup(shopGroup).completableSubscribe())
        } catch (e: RuntimeException) {
            state.value = ShopGroupListFragmentState.OnAddedError(
                e.message ?: "Unexpected failure",
                ShopGroupListFragmentState.OnAddedError.AddErrorCodes.INVALID_NAME
            )
        }
    }

//    fun getAllGroups() = shopRepository.getAllShops()

    fun deleteAllGroups() = disposable.add(shopRepository.deleteAllGroups().completableSubscribe())

//    fun insertShopItem(shopItem: ShopItem) = shopRepository.insertShopItem(shopItem)
}