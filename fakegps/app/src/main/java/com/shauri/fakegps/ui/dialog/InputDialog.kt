package com.shauri.fakegps.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.shauri.fakegps.R
import kotlinx.android.synthetic.main.dialog_int_input.*


class InputDialog(context: Context, val label: Int) :
    Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert) {

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_int_input)
        dialog_input_btnCancel.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                Log.d("CC4", "cicked1")
                dismiss()
            }
        })

        dialog_input_btnSave.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                Log.d("CC4", "cicked2")
                dismiss()
            }
        })
        dialog_input_tvLabel.setText(label)
        dialog_input_etValue.requestFocus()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    }
}