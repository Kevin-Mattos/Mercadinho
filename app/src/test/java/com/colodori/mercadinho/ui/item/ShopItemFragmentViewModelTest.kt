package com.colodori.mercadinho.ui.item

import com.colodori.mercadinho.RxImmediateSchedulerRule
import com.colodori.mercadinho.repository.ShopItemRepository
import com.colodori.mercadinho.repository.entities.ShopGroup
import com.colodori.mercadinho.repository.entities.ShopItem
import com.colodori.mercadinho.viewmodels.ShopItemFragmentViewModel
import com.google.firebase.database.DatabaseError
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopItemFragmentViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    val viewModel: ShopItemFragmentViewModel = ShopItemFragmentViewModel(ShopRepositoryMock())
    val names = listOf("nome", "teste","teste com nome","sem Nome")

    @Before
    fun setup() {
        viewModel.group = ShopGroup(name = "nome")
        viewModel.handle(ShopItemListFragmentIntent.GetAllItensById)
        val names = listOf("nome", "teste","teste com nome","sem Nome")
        viewModel.items.addAll(Array(names.size){ ShopItem("", names[it], false) })
    }

    @Test
    fun SearchGroup_should_filter_groups_by_name() {
        //prepare

        //action
        viewModel.handle(ShopItemListFragmentIntent.OnQuery("nome"))

        //Assert
        Assert.assertTrue(viewModel.state.value is ShopItemListFragmentState.GetAllItensById)
        Assert.assertEquals(names[0], (viewModel.state.value as ShopItemListFragmentState.GetAllItensById).shopItemList[0].name)
        Assert.assertEquals(names[2], (viewModel.state.value as ShopItemListFragmentState.GetAllItensById).shopItemList[1].name)
        Assert.assertEquals(2, (viewModel.state.value as ShopItemListFragmentState.GetAllItensById).shopItemList.size)
    }

    @Test
    fun WHEN_group_list_is_not_empty_add_group_group_list_should_contain_item_at_the_end() {
        //prepare
        val newGroupName = "nome do grupo"

        //action
        viewModel.handle(ShopItemListFragmentIntent.OnAdded(ShopItem(newGroupName,newGroupName,false)))

        //Assert
        Assert.assertEquals(newGroupName, (viewModel.state.value as ShopItemListFragmentState.GetAllItensById).shopItemList[4].name)
    }
    @Test
    fun WHEN_RemoveGroup_list_size_should_decrease() {
        //prepare
        val newGroupName = "nome do grupo"

        //action
        viewModel.handle(ShopItemListFragmentIntent.RemoveItem(ShopItem("0", newGroupName,false).apply { id = "0" }))

        //Assert
        Assert.assertTrue(viewModel.state.value is ShopItemListFragmentState.GetAllItensById)
        Assert.assertEquals(names[1], (viewModel.state.value as ShopItemListFragmentState.GetAllItensById).shopItemList[0].name)
        Assert.assertEquals(names[2], (viewModel.state.value as ShopItemListFragmentState.GetAllItensById).shopItemList[1].name)
        Assert.assertEquals(3, (viewModel.state.value as ShopItemListFragmentState.GetAllItensById).shopItemList.size)
    }
}

class ShopRepositoryMock: ShopItemRepository {

    lateinit var onUpdate: ((List<ShopItem>) -> Unit)
    val items = mutableListOf<ShopItem>()

    init {
        val names = listOf("nome", "teste","teste com nome","sem Nome")
        items.addAll(Array(names.size){ ShopItem("$it", names[it], false).apply { id = "$it" } })
    }

    override fun getItemByShopId(
        itemId: String,
        onUpdate: ((List<ShopItem>) -> Unit)?,
        onCanceled: ((DatabaseError) -> Unit)?
    ) {
        if (onUpdate != null) {
            this.onUpdate = onUpdate
        }
    }

    override fun addItem(item: ShopItem) {
        items.add(item)
        onUpdate(getStuff())
    }

    override fun removeItemFB(item: ShopItem) {
        val id = items.indexOfFirst { item.id == it.id }
        items.removeAt(id)
        onUpdate(getStuff())
    }

    override fun updateItem(item: ShopItem) {
        val item2 = items.firstOrNull { it.id == item.id }
        item2?.bought = item.bought
        onUpdate(getStuff())
    }

    fun getStuff(): MutableList<ShopItem> {
        return items
    }
}