package com.example.mercadinho.viewmodels

import com.example.mercadinho.repository.ShopItemRepository
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.ui.item.ShopItemListFragmentIntent
import com.example.mercadinho.ui.item.ShopItemListFragmentState
import com.example.mercadinho.util.BaseViewModel
import com.example.mercadinho.util.singleSubscribe
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


@HiltViewModel
class ShopItemFragmentViewModel @Inject constructor(private val shopRepository: ShopItemRepository) :
    BaseViewModel<ShopItemListFragmentIntent, ShopItemListFragmentState>() {

    private val TAG: String = "ShopItemFragmentViewModel"
    var groupId = ""
    var items: MutableList<ShopItem> =
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
        shopRepository.updateItem(item)
    }

    private fun removeItem(shopItem: ShopItem) = shopRepository.removeItemFB(shopItem)

    private fun getItemsById() = shopRepository.getItemByShopId(groupId, onUpdate = { result ->

        items = result.toMutableList()
        _state.value = ShopItemListFragmentState.GetAllItensById(items)
    })

    private fun insertShopItem(shopItem: ShopItem) = shopRepository.addItem(shopItem)

    private fun queryGroups(query: String) {
        disposable.add(
            Single.create<List<ShopItem>> {
                val filteredItems = items.filter { it.name.contains(query) }
                it.onSuccess(filteredItems)
            }.singleSubscribe(
                onSuccess = {
                    _state.value = ShopItemListFragmentState.GetAllItensById(it)
                }
            )
        )
    }
}