package com.fidato.inventorymngmnt.ui.master.viewmodel

import android.app.Application
import androidx.lifecycle.liveData
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.constants.Constants.Companion.connect_exception
import com.fidato.inventorymngmnt.constants.Constants.Companion.something_wrong
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.master.adapter.CategoryAdapter
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import java.net.ConnectException

class CategoryViewModel(
    application: Application,
    private val masterRepo: MasterRepository
) : BaseViewModel(application) {

    private val TAG = CategoryViewModel::class.java.canonicalName

    private val context = application.applicationContext

    var arylstCategory: ArrayList<Category>

    lateinit var categoryAdapter: CategoryAdapter

    init {
        arylstCategory = ArrayList<Category>()
    }

    fun getCategory() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val networkValidatorPair = context.isNetworkAvailable
        if (networkValidatorPair.first) {
            try {
                val categoryBaseResponse = masterRepo.getCategory()
                if (!categoryBaseResponse.data.isNullOrEmpty()) {
                    emit(Resource.success(categoryBaseResponse.data))
                } else {
                    if (categoryBaseResponse.error != null
                        && categoryBaseResponse.error.errorMessage != null
                    ) {
                        emit(Resource.error(null, categoryBaseResponse.error.errorMessage))
                    } else {
                        emit(Resource.error(null, something_wrong))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is ConnectException) {
                    emit(Resource.error(null, connect_exception))
                } else {
                    emit(Resource.error(null, e.localizedMessage))
                }

            }
        } else {
            emit(Resource.error(null, networkValidatorPair.second))
        }
    }

    fun setCategoryData(arylstCategory: ArrayList<Category>) {
        this.arylstCategory = arylstCategory
        categoryAdapter.setCategory(this.arylstCategory)
    }

}