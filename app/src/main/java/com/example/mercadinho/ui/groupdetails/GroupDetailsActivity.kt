package com.example.mercadinho.ui.groupdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mercadinho.databinding.ActivityGroupDetailsBinding
import com.example.mercadinho.repository.entities.ShopGroup
import com.example.mercadinho.repository.entities.UserInfo
import com.example.mercadinho.ui.groupdetails.GroupDetailsActivity.Companion.GROUP
import kotlinx.coroutines.flow.collect

class GroupDetailsActivity: AppCompatActivity() {

    val binding by lazy { ActivityGroupDetailsBinding.inflate(layoutInflater) }
    val viewModel: GroupDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getArgs()
        setupObserver()
    }

    private fun setupObserver() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect { state ->
            when (state) {
                GroupDetailsState.InitialState -> {}
                is GroupDetailsState.ShowDetails -> showDetails(state.shopGroup)
                is GroupDetailsState.ShowParticipants -> showParticipants(state.participants)
            }
        }
    }

    private fun showParticipants(participants: List<UserInfo>) {

    }

    private fun showDetails(shopGroup: ShopGroup) {

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