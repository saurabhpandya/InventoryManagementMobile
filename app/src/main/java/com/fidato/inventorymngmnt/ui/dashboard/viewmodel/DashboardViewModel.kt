package com.fidato.inventorymngmnt.ui.dashboard.viewmodel

import android.app.Application
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.dashboard.adapter.DashboardAdapter

class DashboardViewModel(
    application: Application,
    masterRepository: MasterRepository
) : BaseViewModel(application) {
    private val TAG = DashboardViewModel::class.java.canonicalName

    private lateinit var arylstDashboard: ArrayList<String>
    lateinit var dashboardAdapter: DashboardAdapter

    fun getDashboardList(): ArrayList<String> {
        arylstDashboard = ArrayList<String>()
        arylstDashboard.add("Products")
        arylstDashboard.add("Customers")
        arylstDashboard.add("Orders")
        return arylstDashboard
    }

}