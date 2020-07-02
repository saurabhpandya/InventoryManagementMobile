package com.fidato.inventorymngmnt.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.main.master.viewmodel.CategoryViewModel

class ViewModelFactory<T>(private val dataProvider: T, private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}