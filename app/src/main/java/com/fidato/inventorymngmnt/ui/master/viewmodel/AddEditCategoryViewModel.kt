package com.fidato.inventorymngmnt.ui.master.viewmodel

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.base.TextChangeComponent
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_CAT_ID
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_CAT_NAME
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_EDIT_CAT
import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import com.fidato.inventorymngmnt.utility.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditCategoryViewModel(application: Application, val masterRepository: MasterRepository) :
    BaseViewModel(application) {

    private val TAG: String = AddEditCategoryViewModel::class.java.canonicalName.toString()

    private var category: Category = Category()

    var isEdit: Boolean = false

    var errCatId = MutableLiveData<String>("")
    var errCatName = MutableLiveData<String>("")

    var saveCatResponse = MutableLiveData<Resource<CommonResponse>>()

    var isButtonClicked = false

    var mContext = application.applicationContext

    fun setCategory(category: Category) {
        this.category = category
    }

    fun getCategory() = category

    fun setDataFromBundle(catBundle: Bundle?) {
        if (catBundle != null) {
            val catName = catBundle.getString(BUNDLE_CAT_NAME, "")
            val catId = catBundle.getInt(BUNDLE_CAT_ID, -1)
            setCategory(Category(catId, catName))
            isEdit = catBundle.getBoolean(BUNDLE_EDIT_CAT)
        }
    }

    fun validateCatData(): Boolean {
        Log.d(TAG, "Save Category : $category")
        isButtonClicked = true
        var isCatDataValid = false
//        if (category.id.isEmpty()) {
//            isCatDataValid = false
//            errCatId.value = Constants.ERR_CAT_ID
//        } else {
        if (category.name.isEmpty()) {
            isCatDataValid = false
            errCatName.value = Constants.ERR_CAT_NAME
        } else {
            isCatDataValid = true
            val networkAvailblePair = mContext.isNetworkAvailable
            if (networkAvailblePair.first) {
                category.name = category.name.trim()
                saveCategory()
            } else {
                mContext.showToast(networkAvailblePair.second)
            }
        }
//        }
        return isCatDataValid
    }

    fun saveCategory() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            saveCatResponse.value = Resource.loading(null)
        }
        try {
            var saveCategoryResponse = BaseResponse<CommonResponse>()
            if (isEdit) {
                saveCategoryResponse = masterRepository.updateCategory(category)
            } else {
                saveCategoryResponse = masterRepository.saveCategory(category)
            }

            if (saveCategoryResponse.data != null) {
                withContext(Dispatchers.Main) {
                    saveCatResponse.value = Resource.success(saveCategoryResponse.data!!)
                }
            } else {
                val error = saveCategoryResponse.error
                if (error != null) {
                    withContext((Dispatchers.Main)) {
                        saveCatResponse.value = Resource.error(null, error.errorMessage!!)
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                saveCatResponse.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {

    }
}

