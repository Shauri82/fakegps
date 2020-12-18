package com.shauri.fakegps.ui.locations

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.shauri.fakegps.R
import com.shauri.fakegps.database.entity.Location
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.adapter.LocationAdapter
import com.shauri.fakegps.ui.base.BaseActivity
import com.shauri.fakegps.ui.dialog.AlertDialog
import com.shauri.fakegps.ui.router.Router
import kotlinx.android.synthetic.main.activity_locations.*

class LocationsActivity : BaseActivity<LocationsPresenter>(), LocationsUi {

    lateinit var adapter: LocationAdapter

    override fun provideLayoutRes() = R.layout.activity_locations


    override fun providePresenter(router: Router, component: AppComponent?) =
        LocationsPresenter(this, router, component)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        activityLocations_rvLocations.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        );

        activityLocations_rvLocations.layoutManager = LinearLayoutManager(this)
    }

    override fun setLocations(locations: List<Location>) {
        adapter.update(locations)
    }

    override fun prepareList(listener: LocationAdapter.OnLocationClickedListener) {
        adapter = LocationAdapter(listener)
        activityLocations_rvLocations.adapter = adapter
    }

    override fun showConfirmDialog(
        title: Int,
        locationName: String,
        listener: AlertDialog.OnSaveClickedListener
    ) {
        val label = getString(title)
        AlertDialog(this, String.format(label, locationName), listener).show()
    }


}