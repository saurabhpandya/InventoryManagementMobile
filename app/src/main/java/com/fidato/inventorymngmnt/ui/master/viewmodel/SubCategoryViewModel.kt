package com.fidato.inventorymngmnt.ui.master.viewmodel

import android.app.Application
import androidx.lifecycle.liveData
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.data.model.master.SubCategory
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.master.adapter.SubCategoryAdapter
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import kotlinx.coroutines.Dispatchers

class SubCategoryViewModel(
    application: Application,
    private val masterRepo: MasterRepository
) : BaseViewModel(application) {

    private val TAG = SubCategoryViewModel::class.java.canonicalName

    private val context = application.applicationContext

    var arylstSubCategory: ArrayList<SubCategory>

    lateinit var subCategoryAdapter: SubCategoryAdapter

    var catId: String = ""
    var catName: String = ""
    var subCatId: Int = 0

    init {
        arylstSubCategory = ArrayList<SubCategory>()
    }

    fun getSubCategoryByCatId(catId: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val networkValidatorPair = context.isNetworkAvailable
        if (networkValidatorPair.first) {
            try {
                val subCategoryByIdBaseResponse = masterRepo.getSubCategoryByCatId(catId)
                if (subCategoryByIdBaseResponse.data != null) {
                    emit(Resource.success(subCategoryByIdBaseResponse.data))
                } else {
                    if (subCategoryByIdBaseResponse.error != null
                        && subCategoryByIdBaseResponse.error.errorMessage != null
                    ) {
                        emit(Resource.error(null, subCategoryByIdBaseResponse.error.errorMessage))
                    } else {
                        emit(Resource.error(null, Constants.something_wrong))
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

    fun getSubCategoryBySubCatId(catId: Int, subCatId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val networkValidatorPair = context.isNetworkAvailable
        if (networkValidatorPair.first) {
            try {

                val subCategoryByIdBaseResponse =
                    masterRepo.getSubCategoryBySubCatId(catId, subCatId)
                if (subCategoryByIdBaseResponse.data != null) {
                    emit(Resource.success(subCategoryByIdBaseResponse.data))
                } else {
                    if (subCategoryByIdBaseResponse.error != null
                        && subCategoryByIdBaseResponse.error.errorMessage != null
                    ) {
                        emit(Resource.error(null, subCategoryByIdBaseResponse.error.errorMessage))
                    } else {
                        emit(Resource.error(null, Constants.something_wrong))
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

    fun setSubCategoryData(arylstSubCategory: ArrayList<SubCategory>) {
        this.arylstSubCategory = arylstSubCategory
        subCategoryAdapter.setSubCategory(this.arylstSubCategory)
    }

}