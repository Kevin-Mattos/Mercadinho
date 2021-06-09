package com.example.mercadinho.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mercadinho.MainActivity
import com.example.mercadinho.R
import com.example.mercadinho.databinding.CreateCustomDialogBinding
import com.example.mercadinho.databinding.MainFragmentBinding
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.view.adapter.ShopGroupAdapter
import com.example.mercadinho.view.extensions.showToast
import com.example.mercadinho.viewmodels.ShopGroupFragmentViewModel
import com.example.mercadinho.viewmodels.ShopGroupListFragmentIntent
import com.example.mercadinho.viewmodels.ShopGroupListFragmentState

class ShopGroupFragment : Fragment(), ShopGroupAdapter.GroupAction, MainActivity.FabAction {

    private val mBinding by lazy { MainFragmentBinding.inflate(layoutInflater) }
    private val mAdapter by lazy { ShopGroupAdapter(mMainActivity.applicationContext, actions = this) }
    private val mMainActivity: MainActivity by lazy { activity as MainActivity }
    private val mViewModel: ShopGroupFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGroups()
        mViewModel.handle(ShopGroupListFragmentIntent.GetAllGroups)
        setupAdapter()
    }

    override fun onResume() {
        super.onResume()
        mMainActivity.fabCallback = this::fabClicked
    }

    override fun onClick(groupId: Long) = findNavController().run {
        navigate(ShopGroupFragmentDirections.actionShopGroupFragmentToShopItemFragment(groupId))
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

    private fun observeGroups() {
        mViewModel.state.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ShopGroupListFragmentState.GetAllGroups -> updateGroups(it.groupList)
                is ShopGroupListFragmentState.OnAddedError -> showError(it.message, it.code)
            }
        })
    }

    private fun showError(message: String, code: ShopGroupListFragmentState.OnAddedError.AddErrorCodes) {
        Log.e(TAG, message)
        if(code == ShopGroupListFragmentState.OnAddedError.AddErrorCodes.INVALID_NAME)
            showToast(getString(R.string.error_group_invalid_name))
    }

    private fun updateGroups(groupList: LiveData<List<ShopGroup>>) {
        groupList.removeObservers(viewLifecycleOwner)
        groupList.observe(viewLifecycleOwner) {
            mAdapter.update(it)
        }
    }

    companion object{
        const val TAG = "ShopGroupFragment"
    }
}