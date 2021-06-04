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
import com.example.mercadinho.R
import com.example.mercadinho.databinding.MainActivityBinding
import com.example.mercadinho.databinding.MainFragmentBinding
import com.example.mercadinho.repository.entities.ShopGroup

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    val mBinding by lazy {
        MainFragmentBinding.inflate(layoutInflater)
    }

    private val mViewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding.message.text = "texto"

        Log.d("FRAG", "1")
        mViewModel.insertShopGroup(ShopGroup(0, "nome2"))
        mViewModel.getAllGroups().observe(viewLifecycleOwner, Observer {
            it?.forEach {groupShop ->
                Log.d("FRAG", "$groupShop")
            }
        })
        Log.d("FRAG", "3")



        return mBinding.root
    }


}