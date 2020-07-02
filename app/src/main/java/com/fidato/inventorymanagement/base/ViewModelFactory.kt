package com.fidato.inventorymanagement.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fidato.inventorymanagement.data.master.MasterNetworkDataProvider
import com.fidato.inventorymanagement.data.repository.MasterRepository
import com.fidato.inventorymanagement.ui.main.category.CategoryViewModel

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