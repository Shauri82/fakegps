package com.shauri.fakegps.ui.move

import android.os.Bundle
import android.view.View
import com.shauri.fakegps.R
import com.shauri.fakegps.ui.base.BaseActivity
import com.shauri.fakegps.ui.router.Router
import kotlinx.android.synthetic.main.activity_move.*

class MoveActivity : BaseActivity<MovePresenter>(), MoveUi {
    override fun provideLayoutRes() = R.layout.activity_move

    override fun providePresenter(router: Router): MovePresenter = MovePresenter(this, router)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { v: View? ->
            onBackPressed()
        }
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
    }
}