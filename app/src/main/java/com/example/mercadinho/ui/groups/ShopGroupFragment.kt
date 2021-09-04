package com.example.mercadinho.ui.groups

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mercadinho.MainActivity
import com.example.mercadinho.R
import com.example.mercadinho.databinding.CreateCustomDialogBinding
import com.example.mercadinho.databinding.FragmentShopGroupBinding
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.view.extensions.addTextListenter
import com.example.mercadinho.view.extensions.showToast
import com.example.mercadinho.viewmodels.ShopGroupFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ShopGroupFragment : Fragment(), ShopGroupAdapter.GroupAction, MainActivity.FabAction {

    private val binding by lazy { FragmentShopGroupBinding.inflate(layoutInflater) }
    private val adapter by lazy { ShopGroupAdapter(mainActivity.applicationContext, actions = this) }
    private val mainActivity: MainActivity by lazy { activity as MainActivity }
    private val viewModel: ShopGroupFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGroups()
        viewModel.handle(ShopGroupListFragmentIntent.GetAllGroups)
        setupAdapter()
        setupSearch()
        setListenters()
    }

    private fun setListenters() = binding.run {
        botaozinho.setOnClickListener {
            val binding = CreateCustomDialogBinding.inflate(mainActivity.layoutInflater)

            val dialogBuilder = AlertDialog.Builder(mainActivity)

            val dialog = dialogBuilder.setView(binding.root).create()

            with(binding) {
                cancelButton.setOnClickListener {
                    dialog.cancel()
                }

                confirmButton.setOnClickListener {
                    viewModel.handle(ShopGroupListFragmentIntent
                    .JoinGroup(binding.inputName.text.toString()))
//                        .OnAdded(ShopGroup(binding.inputName.text.toString())))
                    dialog.cancel()
                }
            }

            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity.fabCallback = this::fabClicked
    }

    override fun onClick(group: ShopGroup) = findNavController().run {
        navigate(ShopGroupFragmentDirections.actionShopGroupFragmentToShopItemFragment(group))
    }

    override fun onLongClick(group: ShopGroup) {
        viewModel.handle(ShopGroupListFragmentIntent.OnClickShare(group))
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
                viewModel.handle(ShopGroupListFragmentIntent
//                    .JoinGroup(binding.inputName.text.toString()))
                    .OnAdded(ShopGroup(name = binding.inputName.text.toString())))
                dialog.cancel()
            }
        }

        dialog.show()

    }

    private fun setupAdapter() {
        binding.myRecyclerView.adapter = adapter
    }

    private fun observeGroups() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect {
            when (it) {
                is ShopGroupListFragmentState.GetAllGroups -> showGroups(it.groupList)
                is ShopGroupListFragmentState.OnAddedError -> showError(it.message, it.code)
                is ShopGroupListFragmentState.ShareGroup -> {}//copyId(it.group)
            }
        }
    }

    private fun copyId(group: ShopGroup) {
        val clipboard = getSystemService<ClipboardManager>(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText("Groupid:", "Para se juntar ao meu grupo, entre em : ${group.id}")
        clipboard?.setPrimaryClip(clip);
        showToast("Id copiado :D")
    }

    private fun showError(message: String, code: ShopGroupListFragmentState.OnAddedError.AddErrorCodes) {
        Log.e(TAG, message)
        if(code == ShopGroupListFragmentState.OnAddedError.AddErrorCodes.INVALID_NAME)
            showToast(getString(R.string.error_group_invalid_name))
    }

    private fun showGroups(groupList: List<ShopGroup>) {
            adapter.update(groupList)
    }

    private fun setupSearch() {
        binding.groupSearchView.addTextListenter(
            onQuerySubmit = { query ->
                Log.d("onQueryTextSubmit", "$query")
                //query?.let {
                    viewModel.handle(ShopGroupListFragmentIntent.SearchGroup(query?:""))
              //  }

            },
            onTextChange = { text ->
                Log.d("onTextChange", "$text")
                text?.let {

                }
            }
        )
    }


    companion object{
        const val TAG = "ShopGroupFragment"
    }
}