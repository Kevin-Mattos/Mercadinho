package com.example.mercadinho.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.mercadinho.MainActivity
import com.example.mercadinho.databinding.CreateCustomDialogBinding
import com.example.mercadinho.databinding.MainFragmentBinding
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.view.adapter.ShopGroupAdapter
import com.example.mercadinho.view.adapter.ShopItemAdapter
import com.example.mercadinho.viewmodels.ShopGroupFragmentViewModel
import com.example.mercadinho.viewmodels.ShopItemFragmentViewModel
import com.example.mercadinho.viewmodels.ShopItemListFragmentIntent
import com.example.mercadinho.viewmodels.ShopItemListFragmentState

class ShopItemFragment : Fragment(), ShopItemAdapter.ItemAction, MainActivity.FabAction {

    private val args: ShopItemFragmentArgs by navArgs()

    private val mBinding by lazy {
        MainFragmentBinding.inflate(layoutInflater)
    }

    private val mAdapter by lazy {
        ShopItemAdapter(mMainActivity.applicationContext, actions = this)
    }

    private val mMainActivity : MainActivity by lazy {
        activity as MainActivity
    }

    private val mViewModel: ShopItemFragmentViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mMainActivity.fabCallback = this::fabClicked
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.groupId = args.groupId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        //mViewModel.deleteAllGroups()
        setupAdapter()
        mViewModel.handle(ShopItemListFragmentIntent.GetAllItensById)
    }

    override fun onPause() {
        super.onPause()
        mViewModel.handle(ShopItemListFragmentIntent.UpdateItens(mAdapter.items))
    }

    override fun onClick(item: ShopItem) {
        mViewModel.handle(ShopItemListFragmentIntent.RemoveItem(item))
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
                val item = ShopItem(0, mViewModel.groupId, binding.inputName.text.toString(), false)
                mViewModel.handle(ShopItemListFragmentIntent.OnAdded(item))
                dialog.cancel()
            }
        }
        dialog.show()
    }

    private fun setupAdapter() {
        mBinding.myRecyclerView.adapter = mAdapter
    }

    private fun setupObserver() {
        mViewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ShopItemListFragmentState.GetAllItensById -> observeItens(it.shopItemList)
            }
        }
    }

    private fun observeItens(shopItemList: LiveData<List<ShopItem>>) {
        shopItemList.removeObservers(viewLifecycleOwner)
        shopItemList.observe(viewLifecycleOwner) {
            mAdapter.update(it)
        }
    }
}