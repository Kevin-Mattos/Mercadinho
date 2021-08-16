package com.example.mercadinho.util

import android.app.Activity
import android.content.Intent
import androidx.annotation.AnimRes
import com.example.mercadinho.R

fun Activity.startActivitySlide(intent: Intent, requestCode: Int? = null) {
    startActivityTransition(intent, R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
}

fun Activity.startActivityTransition(intent: Intent, @AnimRes idAninIn :Int, @AnimRes idAninOut: Int, requestCode: Int? = null) {

    if(requestCode == null)
        startActivity(intent)
    else startActivityForResult(intent, requestCode)
    overridePendingTransition(idAninIn, idAninOut)

}