package com.merc.mercadao.ui.groups

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.merc.mercadao.databinding.ShopGroupItemviewBinding
import com.merc.mercadao.repository.entities.ShopGroup

class ShopGroupAdapter (private val context: Context, private val groups: MutableList<ShopGroup> = mutableListOf(), private val actions: GroupAction) :
        RecyclerView.Adapter<ShopGroupAdapter.ViewHolder>() {

        interface GroupAction {
            fun onClick(group: ShopGroup)
            fun onLongClick(group: ShopGroup)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ShopGroupItemviewBinding.inflate(LayoutInflater.from(context), parent, false)
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

        class ViewHolder(private val mBinding: ShopGroupItemviewBinding, private val actions: GroupAction) :
            RecyclerView.ViewHolder(mBinding.root) {

            fun bind(shopGroup: ShopGroup) {
                with(mBinding) {
                    groupName.text = shopGroup.name
                    root.setOnClickListener {
                       actions.onClick(shopGroup)
                    }
                    root.setOnLongClickListener {
                        actions.onLongClick(shopGroup)
                        false
                    }
                }
            }
        }
    }