package com.colodori.mercadinho.util

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.colodori.mercadinho.R

fun Activity.startActivitySlide(
    intent: Intent,
    requestCode: Int? = null,
    finalize: Boolean = false
) {
    startActivityTransition(
        intent,
        R.anim.fui_slide_in_right,
        R.anim.fui_slide_out_left,
        requestCode = requestCode,
        finalize = finalize
    )
}

fun Activity.startActivityTransition(
    intent: Intent,
    @AnimRes idAninIn: Int,
    @AnimRes idAninOut: Int,
    requestCode: Int? = null,
    finalize: Boolean = false
) {

    if (requestCode == null)
        startActivity(intent)
    else startActivityForResult(intent, requestCode)
    overridePendingTransition(idAninIn, idAninOut)
    if (finalize)
        finish()
}

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

fun AppCompatActivity.setResultAndFinish(result: Int = Activity.RESULT_OK) {
    setResult(result)
    finish()
}