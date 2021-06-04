package com.example.mercadinho.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mercadinho.MainActivity
import com.example.mercadinho.R
import com.example.mercadinho.databinding.MainActivityBinding
import com.example.mercadinho.databinding.MainFragmentBinding
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.ui.adapter.ShopGroupAdapter

class MainFragment : Fragment(), ShopGroupAdapter.GroupAction {

    companion object {
        fun newInstance() = MainFragment()
    }

    val mBinding by lazy {
        MainFragmentBinding.inflate(layoutInflater)
    }

    val mAdapter by lazy {
        ShopGroupAdapter(mMainActivity.applicationContext, actions = this)
    }

    val mMainActivity : MainActivity by lazy {
        activity as MainActivity
    }

    private val mViewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        Log.d("FRAG", "1")
        mViewModel.insertShopGroup(ShopGroup(0, "nome2"))
        mViewModel.getAllGroups().observe(viewLifecycleOwner, Observer {
            it.forEach {groupShop ->
                Log.d("FRAG", "$groupShop")
            }
            mAdapter.update(it)
        })
        Log.d("FRAG", "3")


        setupAdapter()
        return mBinding.root
    }

    override fun onClick(groupId: Long): String {
        return ""
    }

    private fun setupAdapter() {
        mBinding.myRecyclerView.adapter = mAdapter
    }


}