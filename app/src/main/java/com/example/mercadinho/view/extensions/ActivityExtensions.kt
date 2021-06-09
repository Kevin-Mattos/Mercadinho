package com.example.mercadinho.view.extensions

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.makeTransaction(execute: FragmentTransaction.() -> Unit) {
    val transaction = supportFragmentManager.beginTransaction()

    execute(transaction)
    transaction
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .addToBackStack(null)
        .commit()
}

fun AppCompatActivity.showToast(msg: String) {
    Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
}