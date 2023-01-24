package com.example.mercadinho.view.extensions

import android.content.Intent
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import com.example.mercadinho.R

fun Fragment.showToast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.startActivitySlide(
    intent: Intent,
    requestCode: Int? = null,
) {
    startActivityTransition(
        intent,
        R.anim.fui_slide_in_right,
        R.anim.fui_slide_out_left,
        requestCode = requestCode
    )
}

fun Fragment.startActivityTransition(
    intent: Intent,
    @AnimRes idAninIn: Int,
    @AnimRes idAninOut: Int,
    requestCode: Int? = null,
) {

    if (requestCode == null)
        startActivity(intent)
    else startActivityForResult(intent, requestCode)
    requireActivity().overridePendingTransition(idAninIn, idAninOut)

}