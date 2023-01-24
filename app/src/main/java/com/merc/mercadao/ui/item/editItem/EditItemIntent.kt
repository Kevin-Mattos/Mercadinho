package com.merc.mercadao.ui.item.editItem

import android.os.Bundle

sealed class EditItemIntent {
    data class InitArgs(val args: Bundle): EditItemIntent()
    object DeleteItem: EditItemIntent()
    data class EditItem(val itemName: String): EditItemIntent()
}