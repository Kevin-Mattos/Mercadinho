package com.example.mercadinho.view.extensions

import android.util.Log
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