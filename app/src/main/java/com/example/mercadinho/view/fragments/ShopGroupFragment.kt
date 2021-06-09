package com.example.mercadinho.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mercadinho.MainActivity
import com.example.mercadinho.databinding.CreateCustomDialogBinding
import com.example.mercadinho.databinding.MainFragmentBinding
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.ShopItem
import com.example.mercadinho.view.adapter.ShopGroupAdapter
import com.example.mercadinho.viewmodels.ShopGroupFragmentViewModel
import com.example.mercadinho.viewmodels.ShopGroupListFragmentIntent
import com.example.mercadinho.viewmodels.ShopGroupListFragmentState

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

    private val mMainActivity: MainActivity by lazy {
        activity as MainActivity
    }

    private val mViewModel: ShopGroupFragmentViewModel by lazy {
        createViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observeGroups()
        mViewModel.handle(ShopGroupListFragmentIntent.GetAllGroups)
        setupAdapter()
        return mBinding.root
    }

    override fun onClick(groupId: Long) {
        mMainActivity.replaceThis(groupId)
    }

    override fun onLongClick(group: ShopGroup) {
        mViewModel.handle(ShopGroupListFragmentIntent.RemoveGroup(group))
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
                mViewModel.handle(ShopGroupListFragmentIntent.OnAdded(ShopGroup(0, binding.inputName.text.toString() )))
                dialog.cancel()
            }
        }

        dialog.show()

    }

    private fun setupAdapter() {
        mBinding.myRecyclerView.adapter = mAdapter
    }

    private fun createViewModel(): ShopGroupFragmentViewModel {
        return ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(ShopGroupFragmentViewModel::class.java)
    }

    private fun observeGroups() {
        mViewModel.state.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ShopGroupListFragmentState.GetAllGroups -> updateGroups(it.groupList)
            }
        })
    }

    private fun updateGroups(groupList: LiveData<List<ShopGroup>>) {
        groupList.removeObservers(viewLifecycleOwner)
        groupList.observe(viewLifecycleOwner) {
            mAdapter.update(it)
        }
    }
}