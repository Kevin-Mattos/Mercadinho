package com.example.mercadinho.viewmodels

import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.util.BaseViewModel
import com.example.mercadinho.util.completableSubscribe
import com.example.mercadinho.util.flowableSubscribe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class ShopItemListFragmentState {
    data class GetAllItensById(val shopItemList: List<ShopItem>) : ShopItemListFragmentState()
}

sealed class ShopItemListFragmentIntent {
    object GetAllItensById : ShopItemListFragmentIntent()
    data class OnAdded(val shopItem: ShopItem) : ShopItemListFragmentIntent()
    data class UpdateItens(val shopItems: List<ShopItem>) : ShopItemListFragmentIntent()
    data class RemoveItem(val shopItem: ShopItem) : ShopItemListFragmentIntent()
}

@HiltViewModel
class ShopItemFragmentViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    BaseViewModel<ShopItemListFragmentIntent, ShopItemListFragmentState>() {

    var groupId = 0L

    override fun handle(intent: ShopItemListFragmentIntent) {
        when (intent) {
            is ShopItemListFragmentIntent.GetAllItensById -> getItemsById()
            is ShopItemListFragmentIntent.OnAdded -> insertShopItem(intent.shopItem)
            is ShopItemListFragmentIntent.UpdateItens -> updateAll(intent.shopItems)
            is ShopItemListFragmentIntent.RemoveItem -> removeItem(intent.shopItem)
        }
    }

    private fun removeItem(shopItem: ShopItem) = disposable.add(shopRepository.removeItem(shopItem).completableSubscribe())

    private fun getItemsById() =
        disposable.add(shopRepository.getItemByGroupId(groupId).flowableSubscribe(
            onNext = { items ->
                state.value = ShopItemListFragmentState.GetAllItensById(items)
            }
        ))

    private fun insertShopItem(shopItem: ShopItem) =
        disposable.add(shopRepository.insertShopItem(shopItem).completableSubscribe())

//    private fun getItemById() = shopRepository.getItemByGroupId(groupId)

    private fun updateAll(shopItems: List<ShopItem>) =
        disposable.add(shopRepository.updateAllShopItens(shopItems).completableSubscribe())


}