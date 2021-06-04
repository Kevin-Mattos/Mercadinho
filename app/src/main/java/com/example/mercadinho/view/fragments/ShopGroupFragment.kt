package com.example.mercadinho.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mercadinho.MainActivity
import com.example.mercadinho.databinding.MainFragmentBinding
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.view.adapter.ShopGroupAdapter
import com.example.mercadinho.viewmodels.ShopGroupFragmentViewModel

class ShopGroupFragment : Fragment(), ShopGroupAdapter.GroupAction, MainActivity.FabAction {

    companion object {
        fun newInstance() = ShopGroupFragment()
    }

    private val mBinding by lazy {
        MainFragmentBinding.inflate(layoutInflater)
    }

    private val mAdapter by lazy {
        ShopGroupAdapter(mMainActivity.applicationContext, actions = this)
    }

    private val mMainActivity : MainActivity by lazy {
        activity as MainActivity
    }

    private val mViewModel: ShopGroupFragmentViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(
            ShopGroupFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Log.d("FRAG", "1")

        mViewModel.getAllGroups().observe(viewLifecycleOwner, Observer {
            it.forEach {groupShop ->
                Log.d("FRAG", "$groupShop")
            }
            mAdapter.update(it)
        })

        mViewModel.getAllItems().observe(viewLifecycleOwner, Observer {
            it.forEach {groupItem ->
                Log.d("FRAG", "$groupItem")
            }
        })

        //mViewModel.deleteAllGroups()
        Log.d("FRAG", "3")


        setupAdapter()
        return mBinding.root
    }

    override fun onClick(groupId: Long) {
        mMainActivity.replaceThis(groupId)
    }

    override fun fabClicked() {
        mViewModel.insertShopGroup(ShopGroup(0, "nome2"))
    }

    private fun setupAdapter() {
        mBinding.myRecyclerView.adapter = mAdapter
    }


}