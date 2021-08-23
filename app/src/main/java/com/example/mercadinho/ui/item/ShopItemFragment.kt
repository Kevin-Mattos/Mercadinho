package com.example.mercadinho.ui.item

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.mercadinho.MainActivity
import com.example.mercadinho.databinding.CreateCustomDialogBinding
import com.example.mercadinho.databinding.FragmentShopItemBinding
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.ui.groupdetails.CreateDetailsActivityIntent
import com.example.mercadinho.view.extensions.addTextListenter
import com.example.mercadinho.viewmodels.ShopItemFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ShopItemFragment : Fragment(), ShopItemAdapter.ItemAction, MainActivity.FabAction {

    private val args: ShopItemFragmentArgs by navArgs()
    private val binding by lazy { FragmentShopItemBinding.inflate(layoutInflater) }
    private val adapter by lazy { ShopItemAdapter(mainActivity.applicationContext, actions = this) }
    private val mainActivity : MainActivity by lazy { activity as MainActivity }
    private val viewModel: ShopItemFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.group = args.group
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

    private fun setView() {
        binding.groupName.text = viewModel.group.name
        binding.groupName.setOnClickListener {
            context?.let {
                startActivity(it.CreateDetailsActivityIntent(viewModel.group))
            }
        }
        binding.groupSearchView.addTextListenter(
            onQuerySubmit = { query ->
                Log.d("onQueryTextSubmit", "$query")
                //query?.let {
                viewModel.handle(ShopItemListFragmentIntent.OnQuery(query ?: ""))
                //  }

            },
            onTextChange = { text ->
                Log.d("onTextChange", "$text")
                text?.let {

                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        mainActivity.fabCallback = this::fabClicked
    }

    override fun onClick(item: ShopItem) {
        viewModel.handle(ShopItemListFragmentIntent.RemoveItem(item))
    }

    override fun onCheckClick(item: ShopItem) {
        viewModel.handle(ShopItemListFragmentIntent.UpdateItem(item))
    }

    override fun fabClicked() {

        val binding = CreateCustomDialogBinding.inflate(mainActivity.layoutInflater)

        val dialogBuilder = AlertDialog.Builder(mainActivity)

        val dialog = dialogBuilder.setView(binding.root).create()

        with(binding) {
            cancelButton.setOnClickListener {
                dialog.cancel()
            }

            confirmButton.setOnClickListener {
                val item = ShopItem( viewModel.group.id, binding.inputName.text.toString(), false)
                viewModel.handle(ShopItemListFragmentIntent.OnAdded(item))
                dialog.cancel()
            }
        }
        dialog.show()
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
}