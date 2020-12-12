package com.shauri.fakegps.ui.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.shauri.fakegps.ui.move.MoveActivity

class Router(val activity: AppCompatActivity) {
    fun goToMoveScreen() {
        val i = Intent(activity, MoveActivity::class.java)
        activity.startActivity(i)
    }


}