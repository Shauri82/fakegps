package com.shauri.fakegps.ui.move

import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.base.BasePresenter
import com.shauri.fakegps.ui.router.Router

class MovePresenter(uiRef: MoveUi, router: Router, appComponent: AppComponent?) :
    BasePresenter<MoveUi>(uiRef, router, appComponent) {


    val prefsInteractor = appComponent?.prefsInteractor()


    var randomSelected = prefsInteractor?.isRandomMovement() ?: true;


    fun onInitialized() {
        ui()?.setChecked(prefsInteractor?.isMockMovement() ?: false)
        ui()?.setArc(prefsInteractor?.getArc() ?: 0.0)
    }

    fun onAdvancedClicked() {
        randomSelected = false;
        prefsInteractor?.setRandomMovement(randomSelected)
        ui()?.setAdnavcedSelected()
        ui()?.setRandomUnselected()
        showAdvancedOptions()
    }

    fun onRandomClicked() {
        randomSelected = true;
        prefsInteractor?.setRandomMovement(randomSelected)
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
        prefsInteractor?.setMockMovement(checked)
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

    fun onArcChanged(arc:Double){
        prefsInteractor?.setArc(arc)
    }
}