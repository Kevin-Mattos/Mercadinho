package com.example.mercadinho.view.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}