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

        activityMove_tvAdvanced.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                presenter.onAdvancedClicked()
            }
        })
        activityMove_tvRandom.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                presenter.onRandomClicked()
            }
        })

        activityMove_switchMove.setOnCheckedChangeListener { buttonView, isChecked ->
            presenter.onCheckChanged(
                isChecked
            )
        }
    }


    override fun setRandomEnabled() {
        activityMove_tvRandom.isEnabled = true
    }

    override fun setRandomDisabled() {
        activityMove_tvRandom.isEnabled = false
    }

    override fun setAdvancedEnabled() {
        activityMove_tvAdvanced.isEnabled = true
    }

    override fun setAdvancedDisabled() {
        activityMove_tvAdvanced.isEnabled = false
    }

    override fun setAdnavcedSelected() {
        activityMove_tvAdvanced.isSelected = true
    }

    override fun setAdvancedUnselected() {
        activityMove_tvAdvanced.isSelected = false
    }

    override fun setRandomSelected() {
        activityMove_tvRandom.isSelected = true
    }

    override fun setRandomUnselected() {
        activityMove_tvRandom.isSelected = false
    }

    override fun showAdvancedOptions() {
        activityMove_circleDirection.visibility = View.VISIBLE
        activityMove_tvDirectionLabel.visibility = View.VISIBLE
    }

    override fun hideAdvancedOptions() {
        activityMove_circleDirection.visibility = View.GONE
        activityMove_tvDirectionLabel.visibility = View.GONE
    }

    override fun showRandomOptions() {
        activityMove_tvRandomInfo.visibility = View.VISIBLE
    }

    override fun hideRandomOptions() {
        activityMove_tvRandomInfo.visibility = View.GONE
    }

    override fun hideRandomSelect() {
        activityMove_tvRandom.visibility = View.GONE
    }

    override fun hideAdvancedSelect() {
        activityMove_tvAdvanced.visibility = View.GONE
    }

    override fun showRandomSelect() {
        activityMove_tvRandom.visibility = View.VISIBLE
    }

    override fun showAdvancedSelect() {
        activityMove_tvAdvanced.visibility = View.VISIBLE
    }
}