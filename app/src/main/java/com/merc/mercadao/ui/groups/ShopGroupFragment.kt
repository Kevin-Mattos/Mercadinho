package com.merc.mercadao.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.merc.mercadao.MainActivity
import com.merc.mercadao.R
import com.merc.mercadao.databinding.FragmentShopGroupBinding
import com.merc.mercadao.repository.entities.ShopGroup
import com.merc.mercadao.ui.createCustomInputDialog
import com.merc.mercadao.view.extensions.addTextListenter
import com.merc.mercadao.view.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShopGroupFragment : Fragment(), ShopGroupAdapter.GroupAction {

    private val binding by lazy { FragmentShopGroupBinding.inflate(layoutInflater) }
    private val adapter by lazy {
        ShopGroupAdapter(
            mainActivity.applicationContext,
            actions = this
        )
    }
    private val mainActivity: MainActivity by lazy { activity as MainActivity }
    private val viewModel: ShopGroupFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGroups()
        viewModel.handle(ShopGroupListFragmentIntent.GetAllGroups)
        setupAdapter()
        setupSearch()
        setListenters()
    }

    private fun setListenters() = binding.run {
        botaozinho.setOnClickListener {
           joinGroup()
        }
    }

    private fun joinGroup() {

        requireContext().createCustomInputDialog(
            rightButtonAction = {
                viewModel.handle(
                    ShopGroupListFragmentIntent
                        .JoinGroup(it)
                )
            },
            rightButtonText = R.string.action_join_group,
            textHint = R.string.group_name
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.group_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_group -> {
                createGroup()
                true
            }
            R.id.join_group -> {
                joinGroup()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(group: ShopGroup) = findNavController().run {
        navigate(ShopGroupFragmentDirections.actionShopGroupFragmentToShopItemFragment(group))
    }

    override fun onLongClick(group: ShopGroup) {
        viewModel.handle(ShopGroupListFragmentIntent.OnClickShare(group))
    }

    private fun createGroup() {

        requireContext().createCustomInputDialog(
            rightButtonAction = {
                viewModel.handle(
                    ShopGroupListFragmentIntent
                        .OnAdded(ShopGroup(name = it))
                )
            },
            textHint = R.string.group_name
        )
    }

    private fun setupAdapter() {
        binding.myRecyclerView.adapter = adapter
    }

    private fun observeGroups() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect {
            when (it) {
                is ShopGroupListFragmentState.GetAllGroups -> showGroups(it.groupList)
                is ShopGroupListFragmentState.OnAddedError -> showError(it.message, it.code)
                is ShopGroupListFragmentState.ShareGroup -> {
                }//copyId(it.group)
                is ShopGroupListFragmentState.FailedToJoin -> showError(it.reasonId)
            }
        }
    }

    private fun showError(messageId: Int) {
        showToast(getString(messageId))
    }

    private fun showError(
        message: String,
        code: ShopGroupListFragmentState.OnAddedError.AddErrorCodes
    ) {
        Log.e(TAG, message)
        if (code == ShopGroupListFragmentState.OnAddedError.AddErrorCodes.INVALID_NAME)
            showToast(getString(R.string.error_group_invalid_name))
    }

    private fun showGroups(groupList: List<ShopGroup>) {
        adapter.update(groupList)
    }

    private fun setupSearch() {
        binding.groupSearchView.addTextListenter(
            onQuerySubmit = { query ->
                Log.d("onQueryTextSubmit", "$query")
                //query?.let {
                viewModel.handle(ShopGroupListFragmentIntent.SearchGroup(query ?: ""))
                //  }

            },
            onTextChange = { text ->
                Log.d("onTextChange", "$text")
                text?.let {

                }
            }
        )
    }


    companion object {
        const val TAG = "ShopGroupFragment"
    }
}