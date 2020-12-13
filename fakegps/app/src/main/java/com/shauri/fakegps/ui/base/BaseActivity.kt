package com.shauri.fakegps.ui.base

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.shauri.fakegps.Application
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.router.Router
import kotlinx.android.synthetic.main.activity_move.*

abstract class BaseActivity<Presenter : BasePresenter<out BaseUi>>() : BaseUi,
    AppCompatActivity() {

    lateinit var presenter: Presenter

    @LayoutRes
    protected abstract fun provideLayoutRes(): Int

    protected abstract fun providePresenter(router: Router, component: AppComponent?): Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initPresenter()
        setContentView(provideLayoutRes())
        presenter.onCreate()

    }

    fun initPresenter() {
        val router = Router(this)
        val component: AppComponent? = Application.getComponentFrom(this)
        presenter = providePresenter(router,component)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }


}