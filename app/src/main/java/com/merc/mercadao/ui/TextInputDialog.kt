package com.merc.mercadao.ui

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StringRes
import com.merc.mercadao.R
import com.merc.mercadao.databinding.CreateCustomDialogBinding

class TextInputDialog(
    val context: Context,
    @StringRes val textHint: Int,
    val initialText: String = "",
    @StringRes val rightButtonText: Int = R.string.action_create,
    @StringRes val leftButtonText: Int = R.string.action_cancel,
    val rightButtonAction: (String) -> Unit,
    val leftButtonAction: (() -> Unit)? = null,

    ) {

    fun create() {
        val binding = CreateCustomDialogBinding.inflate(LayoutInflater.from(context))

        val dialogBuilder = AlertDialog.Builder(context)

        val dialog = dialogBuilder.setView(binding.root).create()

        with(binding) {

            cancelButton.text = context.getString(leftButtonText)
            confirmButton.text = context.getString(rightButtonText)
            textInputLayout.setHint(textHint)
            inputName.setText(initialText)


            cancelButton.setOnClickListener {
                leftButtonAction?.invoke()
                dialog.cancel()
            }

            confirmButton.setOnClickListener {
                rightButtonAction(binding.inputName.text.toString())
                dialog.cancel()
            }
        }

        dialog.show()
    }

}

fun Context.createCustomInputDialog(
    @StringRes textHint: Int,
    initialText: String = "",
    @StringRes rightButtonText: Int = R.string.action_create,
    @StringRes leftButtonText: Int = R.string.action_cancel,
    rightButtonAction: (String) -> Unit,
    leftButtonAction: (() -> Unit)? = null,
) {
    TextInputDialog(
        this,
        textHint,
        initialText,
        rightButtonText,
        leftButtonText,
        rightButtonAction,
        leftButtonAction
    ).create()
}
