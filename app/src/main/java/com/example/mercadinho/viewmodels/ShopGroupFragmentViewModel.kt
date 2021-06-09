package com.example.mercadinho.viewmodels

import androidx.lifecycle.LiveData
import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.util.BaseViewModel
import org.koin.java.KoinJavaComponent.inject
import java.lang.RuntimeException


sealed class ShopGroupListFragmentState {
    data class GetAllGroups(val groupList: LiveData<List<ShopGroup>>) : ShopGroupListFragmentState()
    data class OnAddedError(val message: String, val code: AddErrorCodes) : ShopGroupListFragmentState() {
        enum class AddErrorCodes(val code: Int) {
            INVALID_NAME(1)
        }
    }
}

sealed class ShopGroupListFragmentIntent {
    object GetAllGroups : ShopGroupListFragmentIntent()
    data class OnAdded(val shopGroup: ShopGroup) : ShopGroupListFragmentIntent()
    data class RemoveGroup(val group: ShopGroup) : ShopGroupListFragmentIntent()
}

class ShopGroupFragmentViewModel :
    BaseViewModel<ShopGroupListFragmentIntent, ShopGroupListFragmentState>() {

    private val shopRepository by inject(ShopRepository::class.java)

    override fun handle(intent: ShopGroupListFragmentIntent) {
        when (intent) {
            is ShopGroupListFragmentIntent.GetAllGroups -> getAllShopGroups()
            is ShopGroupListFragmentIntent.OnAdded -> insertShopGroup(intent.shopGroup)
            is ShopGroupListFragmentIntent.RemoveGroup -> removeGroup(intent.group)
        }
    }

    private fun removeGroup(group: ShopGroup) {
        shopRepository.removeGroup(group)
    }

    private fun getAllShopGroups() {
        shopRepository.getAllShops {
            state.value = ShopGroupListFragmentState.GetAllGroups(it)
        }
    }

    private fun insertShopGroup(shopGroup: ShopGroup) {
        try {
            shopGroup.validate()
            shopRepository.insertShopGroup(shopGroup)
        } catch (e: RuntimeException) {
            state.value = ShopGroupListFragmentState.OnAddedError(e.message?:"Unexpected failure", ShopGroupListFragmentState.OnAddedError.AddErrorCodes.INVALID_NAME)
        }
    }

//    fun getAllGroups() = shopRepository.getAllShops()

    fun deleteAllGroups() = shopRepository.deleteAllGroups()

//    fun insertShopItem(shopItem: ShopItem) = shopRepository.insertShopItem(shopItem)
}