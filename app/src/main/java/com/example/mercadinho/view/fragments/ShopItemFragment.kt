package com.example.mercadinho.view.fragments

import android.app.AlertDialog
import android.os.Bundle
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
import com.example.mercadinho.view.adapter.ShopItemAdapter
import com.example.mercadinho.viewmodels.ShopItemFragmentViewModel
import com.example.mercadinho.viewmodels.ShopItemListFragmentIntent
import com.example.mercadinho.viewmodels.ShopItemListFragmentState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ShopItemFragment : Fragment(), ShopItemAdapter.ItemAction, MainActivity.FabAction {

    private val args: ShopItemFragmentArgs by navArgs()
    private val mBinding by lazy { FragmentShopItemBinding.inflate(layoutInflater) }
    private val mAdapter by lazy { ShopItemAdapter(mMainActivity.applicationContext, actions = this) }
    private val mMainActivity : MainActivity by lazy { activity as MainActivity }
    private val mViewModel: ShopItemFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.groupId = args.groupId?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupAdapter()
        mViewModel.handle(ShopItemListFragmentIntent.GetAllItensById)
    }

    override fun onResume() {
        super.onResume()
        mMainActivity.fabCallback = this::fabClicked
    }

    override fun onClick(item: ShopItem) {
        mViewModel.handle(ShopItemListFragmentIntent.RemoveItem(item))
    }

    override fun onCheckClick(item: ShopItem) {
        mViewModel.handle(ShopItemListFragmentIntent.UpdateItem(item))
    }

    override fun fabClicked() {

        val binding = CreateCustomDialogBinding.inflate(mMainActivity.layoutInflater)

        val dialogBuilder = AlertDialog.Builder(mMainActivity)

        val dialog = dialogBuilder.setView(binding.root).create()

        with(binding) {
            cancelButton.setOnClickListener {
                dialog.cancel()
            }

            confirmButton.setOnClickListener {
                val item = ShopItem( mViewModel.groupId, binding.inputName.text.toString(), false)
                mViewModel.handle(ShopItemListFragmentIntent.OnAdded(item))
                dialog.cancel()
            }
        }
        dialog.show()
    }

    private fun setupAdapter() {
        mBinding.myRecyclerView.adapter = mAdapter
    }

    private fun setupObserver() = lifecycleScope.launchWhenStarted {
        mViewModel.state.collect {
            when (it) {
                is ShopItemListFragmentState.GetAllItensById -> mAdapter.update(it.shopItemList)
            }
        }
    }
}