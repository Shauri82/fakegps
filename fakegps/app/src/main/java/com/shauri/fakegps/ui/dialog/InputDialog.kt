package com.shauri.fakegps.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.NonNull
import com.shauri.fakegps.R
import kotlinx.android.synthetic.main.dialog_int_input.*


class InputDialog(
    context: Context,
    val label: Int,
    val value: String,
    val maxInt: Int,
    val listener: OnSaveClickedListener
) :
    Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_int_input)
        dialog_input_btnCancel.setOnClickListener { dismiss() }

        dialog_input_btnSave.setOnClickListener {
            if (TextUtils.isEmpty(dialog_input_etValue.text)) {
                val info = String.format(
                    context.getString(R.string.dialog_error),
                    maxInt.toString()
                )
                dialog_input_tvError.setText(info)
                dialog_input_tvError.visibility = View.VISIBLE
            } else if (TextUtils.isDigitsOnly(dialog_input_etValue.text)) {
                val valueI = dialog_input_etValue.text.toString().toInt()
                if (valueI < 1 || valueI > maxInt) {
                    val info = String.format(
                        context.getString(R.string.dialog_error2),
                        maxInt.toString()
                    )
                    dialog_input_tvError.setText(info)
                    dialog_input_tvError.visibility = View.VISIBLE
                } else {
                    dialog_input_tvError.visibility = View.GONE
                    listener.onSaveClicked(dialog_input_etValue.text.toString())
                    dismiss()
                }
            } else {
                dismiss()
            }
        }
        dialog_input_tvLabel.setText(label)
        dialog_input_etValue.setText(value)
        dialog_input_etValue.requestFocus()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    }


    interface OnSaveClickedListener {
        fun onSaveClicked(@NonNull value: String)
    }
}