package com.shauri.fakegps.ui.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import com.shauri.fakegps.R
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.base.BaseActivity
import com.shauri.fakegps.ui.router.Router
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity<AboutPresenter>(), AboutUi {
    override fun provideLayoutRes() = R.layout.activity_about

    override fun providePresenter(router: Router, component: AppComponent?): AboutPresenter =
        AboutPresenter(this, router, component)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { v: View? ->
            onBackPressed()
        }
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        activityAbout_tvTerms.setMovementMethod(LinkMovementMethod.getInstance())
    }

    override fun setVersion(ver: String) {
        val value = getString(R.string.activityAbout_version)
        activityAbout_tvVersion.setText(String.format(value,ver))

    }
}