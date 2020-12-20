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
import kotlinx.android.synthetic.main.dialog_string_input.*

class InputStringDialog(
    context: Context,
    val listener: OnSaveClickedListener
) :
    Dialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_string_input)
        dialog_input_btnCancel.setOnClickListener { dismiss() }

        dialog_input_btnSave.setOnClickListener {
            if (TextUtils.isEmpty(dialog_input_etValue.text)) {
                dialog_input_tvError.setText(context.getString(R.string.dialog_error3))
                dialog_input_tvError.visibility = View.VISIBLE
            } else {
                dialog_input_tvError.visibility = View.GONE
                listener.onSaveClicked(dialog_input_etValue.text.toString())
                dismiss()
            }
        }
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