package com.colodori.mercadinho.view.extensions

import android.view.View
import androidx.appcompat.widget.SearchView

fun SearchView.addTextListenter(onTextChange: ((String?) -> Unit)? = null, onQuerySubmit: ((String?) -> Unit)? = null) {
    this.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            onQuerySubmit?.invoke(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            onTextChange?.invoke(newText)
            onQuerySubmit?.invoke(newText)
            return true
        }
    })
}

fun View.setVisible(visible: Boolean) {
    visibility = if(visible)
        View.VISIBLE
    else
        View.GONE
}