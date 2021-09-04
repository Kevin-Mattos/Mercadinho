package com.example.mercadinho.ui.groupdetails

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.mercadinho.R
import com.example.mercadinho.databinding.ActivityGroupDetailsBinding
import com.example.mercadinho.databinding.CreateCustomDialogBinding
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.UserInfo
import com.example.mercadinho.ui.groupdetails.GroupDetailsActivity.Companion.GROUP
import com.example.mercadinho.ui.groups.ShopGroupListFragmentIntent
import com.example.mercadinho.util.showToast
import com.example.mercadinho.view.extensions.setVisible
import com.example.mercadinho.view.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GroupDetailsActivity: AppCompatActivity() {

    private val binding by lazy { ActivityGroupDetailsBinding.inflate(layoutInflater) }
    private val viewModel: GroupDetailsViewModel by viewModels()
    private val adapter: ShopGroupParticipantsAdapter by lazy {
        val ad = ShopGroupParticipantsAdapter(applicationContext) {
            Log.d("", "${GroupDetailsActivity::class.java.name} $it")
        }
        binding.participantsRv.adapter = ad
        ad
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getArgs()
        setupObserver()
        setListeners()
    }

    private fun setListeners() = binding.run {

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        groupId.setOnLongClickListener {
            viewModel.handle(GroupDetailsIntent.IdClicked)
            false
        }

        leaveGroupBt.setOnClickListener {
            viewModel.handle(GroupDetailsIntent.LeaveGroup)
        }

        addDescriptionIc.setOnClickListener {
            val binding = CreateCustomDialogBinding.inflate(layoutInflater)

            val dialogBuilder = AlertDialog.Builder(this@GroupDetailsActivity)

            val dialog = dialogBuilder.setView(binding.root).create()

            with(binding) {
                cancelButton.setOnClickListener {
                    dialog.cancel()
                }

                binding.inputName.setText(this@GroupDetailsActivity.binding.groupDescription.text)
                binding.textInputLayout.setHint(R.string.description_hint)

                confirmButton.setOnClickListener {
                    viewModel.handle(
                        GroupDetailsIntent.EditGroupDescription
                        (description = binding.inputName.text.toString()))
                    dialog.cancel()
                }
            }

            dialog.show()
        }
    }

    private fun setupObserver() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect { state ->
            when (state) {
                GroupDetailsState.InitialState -> {}
                is GroupDetailsState.ShowDetails -> showDetails(state.shopGroup)
                is GroupDetailsState.ShowParticipants -> showParticipants(state.participants)
                is GroupDetailsState.CopyId -> copyId(state.id)
                GroupDetailsState.LeaveGroup -> leaveGroup()
            }
        }
    }

    private fun leaveGroup() {
        finish()
    }

    private fun showParticipants(participants: List<UserInfo>) {
        adapter.list = participants.toMutableList()
    }

    private fun showDetails(shopGroup: ShopGroup) = binding.run {
        toolbar.title = shopGroup.name
        groupId.text = shopGroup.id

        groupDescription.setVisible(visible = shopGroup.description.isNotEmpty())

        groupDescription.text = shopGroup.description
    }

    private fun copyId(id: String) {
        val clipboard = ContextCompat.getSystemService<ClipboardManager>(
            applicationContext,
            ClipboardManager::class.java
        )
        val clip = ClipData.newPlainText("Groupid:", "$id")
        clipboard?.setPrimaryClip(clip);
        showToast("Id copiado :D")
    }

    private fun getArgs() {
        intent.extras?.let {
            viewModel.handle(GroupDetailsIntent.InitArgs(it))
        } ?: finish()
    }

    companion object {
        const val GROUP = "GROUP"
    }
}

fun Context.CreateDetailsActivityIntent(shopGroup: ShopGroup): Intent {
    return Intent(this, GroupDetailsActivity::class.java).apply {
        putExtra(GROUP, shopGroup)
    }
}