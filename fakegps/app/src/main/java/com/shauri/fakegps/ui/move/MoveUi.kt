package com.shauri.fakegps.ui.move

import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.shauri.fakegps.ui.base.BaseUi

interface MoveUi : BaseUi {

    fun setRandomEnabled()
    fun setRandomDisabled()
    fun setAdvancedEnabled()
    fun setAdvancedDisabled()
    fun setAdnavcedSelected()
    fun setAdvancedUnselected()
    fun setRandomSelected()
    fun setRandomUnselected()
    fun showAdvancedOptions()
    fun hideAdvancedOptions()
    fun showRandomOptions()
    fun hideRandomOptions()
    fun hideRandomSelect()
    fun hideAdvancedSelect()
    fun showRandomSelect()
    fun showAdvancedSelect()
    fun setArc(arc:Double)
    fun setMoveInterval(sec:Int)

}