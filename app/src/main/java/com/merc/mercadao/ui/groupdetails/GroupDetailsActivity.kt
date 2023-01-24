package com.merc.mercadao.ui.groupdetails

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
import com.merc.mercadao.R
import com.merc.mercadao.databinding.ActivityGroupDetailsBinding
import com.merc.mercadao.repository.entities.ShopGroup
import com.merc.mercadao.repository.entities.UserInfo
import com.merc.mercadao.ui.createCustomInputDialog
import com.merc.mercadao.ui.groupdetails.GroupDetailsActivity.Companion.GROUP
import com.merc.mercadao.util.setResultAndFinish
import com.merc.mercadao.util.showToast
import com.merc.mercadao.view.extensions.setVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GroupDetailsActivity : AppCompatActivity() {

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

            this@GroupDetailsActivity.createCustomInputDialog(
                rightButtonAction = {
                    viewModel.handle(
                        GroupDetailsIntent.EditGroupDescription
                            (description = it)
                    )
                },
                textHint = R.string.description_hint,
                rightButtonText = R.string.change_description,
                initialText = this@GroupDetailsActivity.binding.groupDescription.text.toString()
            )
        }
    }

    private fun setupObserver() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect { state ->
            when (state) {
                GroupDetailsState.InitialState -> {
                }
                is GroupDetailsState.ShowDetails -> showDetails(state.shopGroup)
                is GroupDetailsState.ShowParticipants -> showParticipants(state.participants)
                is GroupDetailsState.CopyId -> copyId(state.id)
                GroupDetailsState.LeaveGroup -> leaveGroup()
            }
        }
    }

    private fun leaveGroup() {
        setResultAndFinish()
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