package com.example.mercadinho.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mercadinho.databinding.ShopItemItemviewBinding
import com.example.mercadinho.repository.entities.ShopItem

class ShopItemAdapter (private val context: Context, val items: MutableList<ShopItem> = mutableListOf(), private val actions: ItemAction) :
    RecyclerView.Adapter<ShopItemAdapter.ViewHolder>() {

    interface ItemAction {
        fun onClick(item: ShopItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShopItemItemviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding, actions)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]
        holder.bind(movie)
    }

    fun update(items: List<ShopItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(private val mBinding: ShopItemItemviewBinding, private val actions: ItemAction) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bind(shopItem: ShopItem) {

            with(mBinding) {
                itemName.text = shopItem.name
                bought.isChecked = shopItem.bought
                bought.setOnClickListener {
                    shopItem.bought = bought.isChecked
                }
                root.setOnClickListener {
                    actions.onClick(shopItem)
                }
            }
        }
    }
}