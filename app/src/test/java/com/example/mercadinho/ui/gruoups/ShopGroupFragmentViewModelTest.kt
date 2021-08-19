package com.example.mercadinho.ui.gruoups

import com.example.mercadinho.RxImmediateSchedulerRule
import com.example.mercadinho.repository.ShopGroupRepository
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.ui.groups.ShopGroupListFragmentIntent
import com.example.mercadinho.ui.groups.ShopGroupListFragmentState
import com.example.mercadinho.viewmodels.ShopGroupFragmentViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class ShopGroupFragmentViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    val viewModel: ShopGroupFragmentViewModel = ShopGroupFragmentViewModel(ShopRepositoryMock())

    @Test
    fun SearchGroup_should_filter_groups_by_name() {
        //prepare
        val names = listOf("nome", "teste", "teste com nome", "sem Nome")
        viewModel.groups.value.addAll(Array(names.size) { ShopGroup(name = names[it]) })

        //action
        viewModel.handle(ShopGroupListFragmentIntent.SearchGroup("nome"))

        //Assert
        Assert.assertTrue(viewModel.state.value is ShopGroupListFragmentState.GetAllGroups)
        Assert.assertEquals(
            names[0],
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[0].name
        )
        Assert.assertEquals(
            names[2],
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[1].name
        )
        Assert.assertEquals(
            2,
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList.size
        )
    }

    @Test
    fun WHEN_group_list_is_empty_add_group_group_list_should_contain_item() {
        //prepare
        val newGroupName = "nome do grupo"

        //action
        viewModel.handle(ShopGroupListFragmentIntent.OnAdded(ShopGroup(name = newGroupName)))

        //Assert
        Assert.assertEquals(
            newGroupName,
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[0].name
        )
    }

    @Test
    fun WHEN_group_list_is_not_empty_add_group_group_list_should_contain_item_at_the_end() {
        //prepare
        val names = listOf("nome", "teste", "teste com nome", "sem Nome")
        viewModel.groups.value.addAll(Array(names.size) { ShopGroup(name = names[it]) })
        val newGroupName = "nome do grupo"

        //action
        viewModel.handle(ShopGroupListFragmentIntent.OnAdded(ShopGroup(name = newGroupName)))

        //Assert
        Assert.assertEquals(
            newGroupName,
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[4].name
        )
    }

    @Test
    fun WHEN_RemoveGroup_list_size_should_decrease() {
        //prepare
        val names = listOf("nome", "teste", "teste com nome", "sem Nome")
        viewModel.groups.value.addAll(Array(names.size) { ShopGroup("${it}12123", names[it]) })
        val newGroupName = "nome do grupo"

        //action
        viewModel.handle(ShopGroupListFragmentIntent.RemoveGroup(ShopGroup(name = newGroupName).apply {
            id = "012123"
        }))

        //Assert
        Assert.assertTrue(viewModel.state.value is ShopGroupListFragmentState.GetAllGroups)
        Assert.assertEquals(
            names[1],
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[0].name
        )
        Assert.assertEquals(
            names[2],
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[1].name
        )
        Assert.assertEquals(
            3,
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList.size
        )
    }

    @Test
    fun WHEN_leave_group_list_size_should_decrease() {
        //prepare
        val names = listOf("nome", "teste", "teste com nome", "sem Nome")
        viewModel.groups.value.addAll(Array(names.size) { ShopGroup("${it}12123", names[it]) })
        val newGroupName = "nome do grupo"

        //action
        viewModel.handle(ShopGroupListFragmentIntent.LeaveGroup(ShopGroup(name = newGroupName).apply {
            id = "012123"
        }))

        //Assert
        Assert.assertTrue(viewModel.state.value is ShopGroupListFragmentState.GetAllGroups)
        Assert.assertEquals(
            names[1],
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[0].name
        )
        Assert.assertEquals(
            names[2],
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[1].name
        )
        Assert.assertEquals(
            3,
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList.size
        )
    }

    @Test
    fun WHEN_join_group_list_size_should_increase() {
        //prepare
        val names = listOf("nome", "teste", "teste com nome", "sem Nome")
        viewModel.groups.value.addAll(Array(names.size) { ShopGroup("${it}12123", names[it]) })
        val newGroupName = "nome do grupo"

        //action
        viewModel.handle(ShopGroupListFragmentIntent.JoinGroup(newGroupName))

        //Assert
        Assert.assertTrue(viewModel.state.value is ShopGroupListFragmentState.GetAllGroups)
        Assert.assertEquals(
            names[0],
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[0].name
        )
        Assert.assertEquals(
            names[1],
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[1].name
        )
        Assert.assertEquals(
            newGroupName,
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList[4].name
        )
        Assert.assertEquals(
            5,
            (viewModel.state.value as ShopGroupListFragmentState.GetAllGroups).groupList.size
        )
    }
}

class ShopRepositoryMock : ShopGroupRepository {

    lateinit var onGroupAdded: (ShopGroup) -> Unit
    lateinit var onGroupChanged: (ShopGroup) -> Unit
    lateinit var onGroupRemoved: (String?) -> Unit

    override fun initStuff(
        onGroupAdded: (ShopGroup) -> Unit,
        onGroupChanged: (ShopGroup) -> Unit,
        onGroupRemoved: (String?) -> Unit
    ) {
        this.onGroupAdded = onGroupAdded
        this.onGroupChanged = onGroupChanged
        this.onGroupRemoved = onGroupRemoved
    }

    override fun addGroupFB(group: ShopGroup) {
        onGroupAdded(ShopGroup(group.id, group.name))
    }

    override fun removeGroupFB(group: ShopGroup) {
        onGroupRemoved(group.id)
    }

    override fun joinGroup(groupId: String, failedToJoin: (() -> Unit)?) {
        onGroupAdded(ShopGroup(groupId, groupId))
    }

    override fun leaveGroup(group: ShopGroup) {
        onGroupRemoved(group.id)
    }
}