package com.shauri.fakegps.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.annotation.StringRes
import com.shauri.fakegps.R
import kotlinx.android.synthetic.main.dialog_alert.*

class AlertDialog(
    context: Context,
    val value:String,
    val listener: OnSaveClickedListener
) :
    Dialog(context, R.style.Theme_AppCompat_Light_Dialog_Alert) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_alert)
        dialog_alert_btnCancel.setOnClickListener { dismiss() }

        dialog_alert_btnOk.setOnClickListener {
            listener.onConfirmClicked()
            dismiss()

        }
        dialog_alert_tvLabel.setText(value)
    }


    interface OnSaveClickedListener {
        fun onConfirmClicked()
    }
}