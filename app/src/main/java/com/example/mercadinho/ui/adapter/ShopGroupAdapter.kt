package com.example.mercadinho.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mercadinho.databinding.ShopGroupItemViewBinding
import com.example.mercadinho.repository.entities.ShopGroup

class ShopGroupAdapter (val context: Context, private val groups: MutableList<ShopGroup> = mutableListOf(), private val actions: GroupAction) :
        RecyclerView.Adapter<ShopGroupAdapter.ViewHolder>() {

        interface GroupAction {
            fun onClick(groupId: Long): String
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ShopGroupItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
            return ViewHolder(binding, actions)
        }

        override fun getItemCount() = groups.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val movie = groups[position]
            holder.bind(movie)
        }

        fun update(groups: List<ShopGroup>) {
            this.groups.clear()
            this.groups.addAll(groups)
            notifyDataSetChanged()
        }

        class ViewHolder(private val mBinding: ShopGroupItemViewBinding, private val actions: GroupAction) :
            RecyclerView.ViewHolder(mBinding.root) {
            fun bind(movie: ShopGroup) {
                with(mBinding) {
                    textView.text = movie.name
                }
            }
        }
    }