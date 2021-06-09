package com.example.mercadinho.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.util.BaseViewModel
import org.koin.java.KoinJavaComponent

sealed class ShopItemListFragmentState {
    data class GetAllItensById(val shopItemList: LiveData<List<ShopItem>>): ShopItemListFragmentState()
}

sealed class ShopItemListFragmentIntent {
    object GetAllItensById: ShopItemListFragmentIntent()
    data class OnAdded(val shopItem: ShopItem): ShopItemListFragmentIntent()
    data class UpdateItens(val shopItems: List<ShopItem>): ShopItemListFragmentIntent()
}

class ShopItemFragmentViewModel : BaseViewModel<ShopItemListFragmentIntent, ShopItemListFragmentState>() {

    private val shopRepository by KoinJavaComponent.inject(ShopRepository::class.java)

    var groupId = 0L

    override fun handle(intent: ShopItemListFragmentIntent) {
        when(intent) {
            is ShopItemListFragmentIntent.GetAllItensById -> getItemById2()
            is ShopItemListFragmentIntent.OnAdded -> insertShopItem(intent.shopItem)
            is ShopItemListFragmentIntent.UpdateItens -> updateAll(intent.shopItems)
        }
    }

    private fun getItemById2() {
         shopRepository.getItemByGroupId(groupId) {
             state.value = ShopItemListFragmentState.GetAllItensById(it)
        }
    }

    private fun insertShopItem(shopItem: ShopItem) = shopRepository.insertShopItem(shopItem)

    private fun getItemById() = shopRepository.getItemByGroupId(groupId)

    private fun updateAll(shopItems: List<ShopItem>) = shopRepository.updateAllShopItens(shopItems)


}