package com.shauri.fakegps.ui.move

import com.shauri.fakegps.ui.base.BasePresenter
import com.shauri.fakegps.ui.router.Router

class MovePresenter(uiRef: MoveUi, router: Router) : BasePresenter<MoveUi>(uiRef, router) {

    var randomSelected = true;

    override fun onCreate() {
        super.onCreate()
        onCheckChanged(false)
    }


    fun onAdvancedClicked() {
        randomSelected = false;
        ui()?.setAdnavcedSelected()
        ui()?.setRandomUnselected()
        showAdvancedOptions()
    }

    fun onRandomClicked() {
        randomSelected = true;
        ui()?.setRandomSelected()
        ui()?.setAdvancedUnselected()
        showRandomOptions()
    }

    fun showRandomOptions() {
        ui()?.hideAdvancedOptions()
        ui()?.showRandomOptions()
    }

    fun showAdvancedOptions() {
        ui()?.showAdvancedOptions()
        ui()?.hideRandomOptions()
    }

    fun onCheckChanged(checked: Boolean) {
        if (checked) {
            ui()?.showRandomSelect()
            ui()?.showAdvancedSelect()
            if (randomSelected) {
                onRandomClicked()
            } else {
                onAdvancedClicked()
            }
        } else {
            ui()?.hideAdvancedOptions()
            ui()?.hideRandomOptions()
            ui()?.hideAdvancedSelect()
            ui()?.hideRandomSelect()
        }
    }
}