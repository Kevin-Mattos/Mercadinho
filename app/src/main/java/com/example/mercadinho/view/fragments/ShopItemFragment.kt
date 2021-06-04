package com.example.mercadinho.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mercadinho.MainActivity
import com.example.mercadinho.databinding.MainFragmentBinding
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.view.adapter.ShopGroupAdapter
import com.example.mercadinho.view.adapter.ShopItemAdapter
import com.example.mercadinho.viewmodels.ShopGroupFragmentViewModel
import com.example.mercadinho.viewmodels.ShopItemFragmentViewModel


const val GROUP_ID_KEY = "KEY:GROUP_ID"
class ShopItemFragment : Fragment(), ShopItemAdapter.ItemAction, MainActivity.FabAction {

    companion object {
        fun newInstance() = ShopItemFragment()
    }

    private val mBinding by lazy {
        MainFragmentBinding.inflate(layoutInflater)
    }

    private val mAdapter by lazy {
        ShopItemAdapter(mMainActivity.applicationContext, actions = this)
    }

    private val mMainActivity : MainActivity by lazy {
        activity as MainActivity
    }

    private val mViewModel: ShopItemFragmentViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(
            ShopItemFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mViewModel.groupId = it.getLong(GROUP_ID_KEY, -1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Log.d("FRAG", "1")

        mViewModel.getItemById().observe(viewLifecycleOwner, Observer {
            it.forEach {groupItem ->
                Log.d("FRAG", "$groupItem")
            }
            mAdapter.update(it)
        })

        //mViewModel.deleteAllGroups()
        Log.d("FRAG", "3")


        setupAdapter()
        return mBinding.root
    }

    override fun onPause() {
        super.onPause()
        mViewModel.updateAll(mAdapter.items)
    }

    override fun onClick(item: ShopItem) {
        mViewModel.insertShopItem(item)
    }

    override fun fabClicked() {
        mViewModel.insertShopItem(ShopItem(0, mViewModel.groupId, "eae", false))
    }

    private fun setupAdapter() {
        mBinding.myRecyclerView.adapter = mAdapter
    }


}