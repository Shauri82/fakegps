package com.shauri.fakegps.ui.base

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.shauri.fakegps.ui.router.Router

abstract class BaseActivity<Presenter : BasePresenter<out BaseUi>>() : BaseUi,
    AppCompatActivity() {

    lateinit var presenter: Presenter

    @LayoutRes
    protected abstract fun provideLayoutRes(): Int

    protected abstract fun providePresenter(router: Router): Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initPresenter()
        setContentView(provideLayoutRes())
        presenter.onCreate()

    }

    fun initPresenter() {
        val router = Router(this)
        presenter = providePresenter(router)
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