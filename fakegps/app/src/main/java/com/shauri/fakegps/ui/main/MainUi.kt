package com.shauri.fakegps.ui.main

import com.shauri.fakegps.ui.base.BaseUi
import com.shauri.fakegps.ui.dialog.InputStringDialog

interface MainUi : BaseUi {
    fun closeMenu()
    fun showDialog(listener:InputStringDialog.OnSaveClickedListener)
}