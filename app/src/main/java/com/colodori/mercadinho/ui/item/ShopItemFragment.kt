package com.colodori.mercadinho.ui.item

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.colodori.mercadinho.MainActivity
import com.colodori.mercadinho.R
import com.colodori.mercadinho.databinding.FragmentShopItemBinding
import com.colodori.mercadinho.repository.entities.ShopItem
import com.colodori.mercadinho.ui.createCustomInputDialog
import com.colodori.mercadinho.ui.groupdetails.CreateDetailsActivityIntent
import com.colodori.mercadinho.ui.item.editItem.createEditItemIntent
import com.colodori.mercadinho.view.extensions.addTextListenter
import com.colodori.mercadinho.view.extensions.startActivitySlide
import com.colodori.mercadinho.viewmodels.ShopItemFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ShopItemFragment : Fragment(), ShopItemAdapter.ItemAction {

    private val args: ShopItemFragmentArgs by navArgs()
    private val binding by lazy { FragmentShopItemBinding.inflate(layoutInflater) }
    private val adapter by lazy { ShopItemAdapter(mainActivity.applicationContext, actions = this) }
    private val mainActivity : MainActivity by lazy { activity as MainActivity }
    private val viewModel: ShopItemFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.group = args.group
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupAdapter()
        viewModel.handle(ShopItemListFragmentIntent.GetAllItensById)
        setView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_item -> {
                addItem()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setView() {
        binding.groupName.text = viewModel.group.name
        binding.groupName.setOnClickListener {
            context?.let { context ->
                startActivitySlide(
                    context.CreateDetailsActivityIntent(viewModel.group),
                    requestCode = GROUP_DETAILS_CODE
                )
            }
        }
        binding.groupSearchView.addTextListenter(
            onQuerySubmit = { query ->
                viewModel.handle(ShopItemListFragmentIntent.OnQuery(query ?: ""))
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GROUP_DETAILS_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    override fun onClick(item: ShopItem) {
        startActivitySlide(
            requireContext().createEditItemIntent(
                item = item
            )
        )
    }

    override fun onCheckClick(item: ShopItem) {
        viewModel.handle(ShopItemListFragmentIntent.UpdateItem(item))
    }

    fun addItem() {
        requireContext().createCustomInputDialog(
            rightButtonAction = {
                val item = ShopItem( viewModel.group.id, it, false)
                viewModel.handle(ShopItemListFragmentIntent.OnAdded(item))
            },
            textHint = R.string.item_name
        )
    }

    private fun setupAdapter() {
        binding.myRecyclerView.adapter = adapter
    }

    private fun setupObserver() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect {
            when (it) {
                is ShopItemListFragmentState.GetAllItensById -> adapter.update(it.shopItemList)
            }
        }
    }
    companion object {
        const val GROUP_DETAILS_CODE = 400
    }
}