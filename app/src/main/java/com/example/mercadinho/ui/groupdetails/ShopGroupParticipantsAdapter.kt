package com.example.mercadinho.ui.groupdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mercadinho.databinding.GroupParticipantItemviewBinding
import com.example.mercadinho.repository.entities.UserInfo

class ShopGroupParticipantsAdapter(val context: Context, val onUserClick: (UserInfo) -> Unit) :
    RecyclerView.Adapter<ShopGroupParticipantsAdapter.ViewHolder>() {

    var list: MutableList<UserInfo> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GroupParticipantItemviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(val binding: GroupParticipantItemviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userInfo: UserInfo) {
            binding.userName.text = userInfo.nickName?: userInfo.name
            Glide.with(binding.root).load(userInfo.profileUrl).into(binding.userProfileImg)
        }
    }
}