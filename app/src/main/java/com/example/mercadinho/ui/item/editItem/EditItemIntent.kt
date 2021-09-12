package com.example.mercadinho.ui.item.editItem

import android.os.Bundle
import com.example.mercadinho.repository.entities.ShopItem

sealed class EditItemIntent {
    data class InitArgs(val args: Bundle): EditItemIntent()
    object DeleteItem: EditItemIntent()
    data class EditItem(val itemName: String): EditItemIntent()
}