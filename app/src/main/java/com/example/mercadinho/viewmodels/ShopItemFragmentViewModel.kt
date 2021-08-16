package com.example.mercadinho.viewmodels

import com.example.mercadinho.repository.ShopRepository
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.util.BaseViewModel
import com.example.mercadinho.util.singleSubscribe
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

sealed class ShopItemListFragmentState {
    data class GetAllItensById(val shopItemList: List<ShopItem>) : ShopItemListFragmentState()
}

sealed class ShopItemListFragmentIntent {
    object GetAllItensById : ShopItemListFragmentIntent()
    data class OnQuery(val query: String) : ShopItemListFragmentIntent()
    data class OnAdded(val shopItem: ShopItem) : ShopItemListFragmentIntent()
    data class RemoveItem(val shopItem: ShopItem) : ShopItemListFragmentIntent()
    data class UpdateItem(val item: ShopItem) : ShopItemListFragmentIntent()
}

@HiltViewModel
class ShopItemFragmentViewModel @Inject constructor(private val shopRepository: ShopRepository) :
    BaseViewModel<ShopItemListFragmentIntent, ShopItemListFragmentState>() {

    private val TAG: String = "ShopItemFragmentViewModel"
    var groupId = ""
    var items : MutableList<ShopItem> =
        mutableListOf()

    override val initialState: ShopItemListFragmentState
        get() = ShopItemListFragmentState.GetAllItensById(emptyList())

    override fun handle(intent: ShopItemListFragmentIntent) {
        when (intent) {
            is ShopItemListFragmentIntent.GetAllItensById -> getItemsById()
            is ShopItemListFragmentIntent.OnAdded -> insertShopItem(intent.shopItem)
            is ShopItemListFragmentIntent.RemoveItem -> removeItem(intent.shopItem)
            is ShopItemListFragmentIntent.UpdateItem -> updateItem(intent.item)
            is ShopItemListFragmentIntent.OnQuery -> queryGroups(intent.query)
        }
    }

    private fun updateItem(item: ShopItem) {
        shopRepository.updateItemFB(item)
    }

    private fun removeItem(shopItem: ShopItem) = shopRepository.removeItemFB(shopItem)//disposable.add(shopRepository.removeItem(shopItem).completableSubscribe())

    private fun getItemsById() = shopRepository.getItemByShopId(groupId, onUpdate = { result ->
        result?.let {
            items = it.map { map -> ShopItem.fromMap(map.key, it[map.key] as HashMap<String, Any>) }.toMutableList()
            _state.value = ShopItemListFragmentState.GetAllItensById(items)
        }
    })

    private fun insertShopItem(shopItem: ShopItem) = shopRepository.addItem(shopItem)


    private fun queryGroups(query: String) {
        disposable.add(
            Single.create<List<ShopItem>> {
                val filteredGroups = items.filter { it.name.contains(query) }
                it.onSuccess(filteredGroups)
            }.singleSubscribe(
                onSuccess = {
                    _state.value = ShopItemListFragmentState.GetAllItensById(it)
                }
            )
        )
    }
}