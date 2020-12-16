package com.shauri.fakegps.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.huawei.hms.api.HuaweiApiAvailability
import com.shauri.fakegps.R
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.base.BaseActivity
import com.shauri.fakegps.ui.router.Router
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity<SettingsPresenter>(), SettingsUi {
    override fun provideLayoutRes(): Int = R.layout.activity_settings

    override fun providePresenter(router: Router, component: AppComponent?): SettingsPresenter =
        SettingsPresenter(this, router, component)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { v: View? ->
            onBackPressed()
        }
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        presenter.afterAvailabilityChecked(
            isGooglePlayServicesAvailable(),
            isHuaweiServicesAvailable()
        )
        activitySettings_sHms.setOnCheckedChangeListener { _, isChecked ->
            presenter.onHmsCheckChanged(
                isChecked
            )
        }
        activitySettings_sGms.setOnCheckedChangeListener { _, isChecked ->
            presenter.onGmsCheckChanged(
                isChecked
            )
        }
    }

    override fun setInterval(interval: Int) {
        activitySettings_etInterval.setText(interval.toString())
    }

    override fun setAccuracy(accuracy: Int) {
        activitySettings_etAccuracy.setText(accuracy.toString())
    }

    override fun enableGooglePlayServices() {
        activitySettings_sGms.isEnabled = true
        activitySettings_tvGmsError.visibility = View.GONE
    }

    override fun enableHuaweiServices() {
        activitySettings_sHms.isEnabled = true
        activitySettings_tvHmsError.visibility = View.GONE
    }

    override fun disableGooglePlayServices() {
        activitySettings_sGms.isEnabled = false
        activitySettings_tvGmsError.visibility = View.VISIBLE
    }

    override fun disableHuaweiPlayServices() {
        activitySettings_sHms.isEnabled = false
        activitySettings_tvHmsError.visibility = View.VISIBLE
    }

    override fun setGmsChecked(checked: Boolean) {
        activitySettings_sGms.isChecked = checked
    }

    override fun setHmsChecked(checked: Boolean) {
        activitySettings_sHms.isChecked = checked
    }

    fun Context.isGooglePlayServicesAvailable(): Boolean =
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS

    fun Context.isHuaweiServicesAvailable(): Boolean =
        HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(this) == com.huawei.hms.api.ConnectionResult.SUCCESS
}