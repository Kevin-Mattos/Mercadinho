package com.example.mercadinho.viewmodels

import androidx.lifecycle.LiveData
import com.example.mercadinho.repository.ShopRepository
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
    data class RemoveItem(val shopItem: ShopItem): ShopItemListFragmentIntent()
}

class ShopItemFragmentViewModel : BaseViewModel<ShopItemListFragmentIntent, ShopItemListFragmentState>() {

    private val shopRepository by KoinJavaComponent.inject(ShopRepository::class.java)

    var groupId = 0L

    override fun handle(intent: ShopItemListFragmentIntent) {
        when(intent) {
            is ShopItemListFragmentIntent.GetAllItensById -> getItemsById()
            is ShopItemListFragmentIntent.OnAdded -> insertShopItem(intent.shopItem)
            is ShopItemListFragmentIntent.UpdateItens -> updateAll(intent.shopItems)
            is ShopItemListFragmentIntent.RemoveItem -> removeItem(intent.shopItem)
        }
    }

    private fun removeItem(shopItem: ShopItem) {
        shopRepository.removeItem(shopItem)
    }

    private fun getItemsById() {
         shopRepository.getItemByGroupId(groupId) {
             state.value = ShopItemListFragmentState.GetAllItensById(it)
        }
    }

    private fun insertShopItem(shopItem: ShopItem) = shopRepository.insertShopItem(shopItem)

//    private fun getItemById() = shopRepository.getItemByGroupId(groupId)

    private fun updateAll(shopItems: List<ShopItem>) = shopRepository.updateAllShopItens(shopItems)


}