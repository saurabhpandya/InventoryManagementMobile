package com.fidato.inventorymanagement.ui.main.category

import android.app.Application
import androidx.lifecycle.liveData
import com.fidato.inventorymanagement.base.BaseViewModel
import com.fidato.inventorymanagement.constants.Constants.Companion.something_wrong
import com.fidato.inventorymanagement.data.model.master.Category
import com.fidato.inventorymanagement.data.repository.MasterRepository
import com.fidato.inventorymanagement.ui.main.category.adapter.CategoryAdapter
import com.fidato.inventorymanagement.utility.Resource
import com.fidato.inventorymanagement.utility.isNetworkAvailable
import kotlinx.coroutines.Dispatchers

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
                emit(Resource.error(null, e.localizedMessage))
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