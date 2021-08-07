package com.example.mercadinho.viewmodels

import android.util.Log
import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.util.BaseViewModel
import com.example.mercadinho.util.completableSubscribe
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
    data class UpdateItem(val item: ShopItem) : ShopItemListFragmentIntent()
}

@HiltViewModel
class ShopItemFragmentViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    BaseViewModel<ShopItemListFragmentIntent, ShopItemListFragmentState>() {

    private val TAG: String = "ShopItemFragmentViewModel"
    var groupId = ""
    override val initialState: ShopItemListFragmentState
        get() = ShopItemListFragmentState.GetAllItensById(emptyList())

    override fun handle(intent: ShopItemListFragmentIntent) {
        when (intent) {
            is ShopItemListFragmentIntent.GetAllItensById -> getItemsById()
            is ShopItemListFragmentIntent.OnAdded -> insertShopItem(intent.shopItem)
            is ShopItemListFragmentIntent.UpdateItens -> updateAll(intent.shopItems)
            is ShopItemListFragmentIntent.RemoveItem -> removeItem(intent.shopItem)
            is ShopItemListFragmentIntent.UpdateItem -> updateItem(intent.item)
        }
    }

    private fun updateItem(item: ShopItem) {
        shopRepository.updateItemFB(item)
    }

    private fun removeItem(shopItem: ShopItem) = shopRepository.removeItemFB(shopItem)//disposable.add(shopRepository.removeItem(shopItem).completableSubscribe())

    private fun getItemsById() = shopRepository.getItemByShopId(groupId, onUpdate = { result ->
        result?.let {
            Log.d(TAG, "$it")
            _state.value = ShopItemListFragmentState.GetAllItensById(it.map { map -> ShopItem.fromMap(map.key, it[map.key] as HashMap<String, Any>) })
        }
    })
//        disposable.add(shopRepository.getItemByGroupId(groupId).flowableSubscribe(
//            onNext = { items ->
//                state.value = ShopItemListFragmentState.GetAllItensById(items)
//            }
//        ))

    private fun insertShopItem(shopItem: ShopItem) = shopRepository.addItem(shopItem)
//        disposable.add(shopRepository.insertShopItem(shopItem).completableSubscribe())

//    private fun getItemById() = shopRepository.getItemByGroupId(groupId)

    private fun updateAll(shopItems: List<ShopItem>) = 0f

//        disposable.add(shopRepository.updateAllShopItens(shopItems).completableSubscribe())


}