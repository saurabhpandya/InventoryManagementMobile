package com.fidato.inventorymngmnt.ui.master.viewmodel

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.base.TextChangeComponent
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_SUB_CAT
import com.fidato.inventorymngmnt.constants.Constants.Companion.ERR_SELECT_CAT
import com.fidato.inventorymngmnt.constants.Constants.Companion.ERR_SUB_CAT_NAME
import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.data.model.master.SubCategory
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import com.fidato.inventorymngmnt.utility.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditSubCategoryViewModel(
    application: Application,
    var masterRepository: MasterRepository
) : BaseViewModel(application) {
    private val TAG: String = AddEditSubCategoryViewModel::class.java.canonicalName.toString()

    private var category: Category = Category()
    private var subCategory: SubCategory = SubCategory()

    private var newSubCategory: SubCategory = SubCategory()

    var selCatIndex = 0
    var selSubCatIndex = 0

    var isEdit: Boolean = false

    var errCat = MutableLiveData<String>("")
    var errSubCat = MutableLiveData<String>("")
    var errNewSubCat = MutableLiveData<String>("")

    var saveSubCatResponse = MutableLiveData<Resource<CommonResponse>>()

    var isButtonClicked = false

    var mContext = application.applicationContext

    private var categoryList = ArrayList<Category>()
    private var subCategoryList = ArrayList<SubCategory>()

    var categoryAdapter =
        ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice)
    var subCategoryAdapter =
        ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice)

    private fun setCategory(category: Category) {
        this.category = category
    }

    fun getCategory() = category

    private fun setSubCategory(subCategory: SubCategory) {
        this.subCategory = subCategory
    }

    fun getSubCategory() = subCategory

    private fun setNewSubCategory(newSubCategory: SubCategory) {
        this.newSubCategory = newSubCategory
    }

    fun getNewSubCategory() = newSubCategory

    fun getCategoryList() = categoryList
    fun getSubCategoryList() = subCategoryList

    fun setDataFromBundle(subCatBundle: Bundle?) {
        if (subCatBundle != null) {
            isEdit = subCatBundle.getBoolean(Constants.BUNDLE_EDIT_SUB_CAT)
            if (isEdit) {
                newSubCategory = subCatBundle.getParcelable<SubCategory>(BUNDLE_SUB_CAT)!!
                subCategory = newSubCategory
                category.id = newSubCategory.catId
            } else {
                subCategory = subCatBundle.getParcelable<SubCategory>(BUNDLE_SUB_CAT)!!
                category.id = subCategory.catId
            }
        }
    }

    fun getAllCategories() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val categoriesResponse = masterRepository.getCategory()
            if (categoriesResponse.data != null) {
                categoryList = categoriesResponse.data
                categoryAdapter.clear()
                selSubCatIndex = -1
                for (category in categoryList) {
                    categoryAdapter.add(category.name)
                }
                selectCategoryToAdapter()
                emit(Resource.success(categoryList))
            } else {
                val error = categoriesResponse.error
                if (error != null) {
                    emit(Resource.error(null, error.errorMessage!!))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun getSubCategories(catId: Int, subCatId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            if (catId > 0) {
                if (subCatId <= 0) {
                    // get sub category from sub category
                    val subCatByCatIdResponse = masterRepository.getSubCategoryByCatId(catId)
                    if (subCatByCatIdResponse.data != null) {
                        subCategoryList = ArrayList<SubCategory>()
                        subCategoryList.add(
                            SubCategory(
                                id = newSubCategory.id,
                                subCatId = 0,
                                name = "None",
                                catId = newSubCategory.catId
                            )
                        )
                        subCategoryList.addAll(subCatByCatIdResponse.data)
                        subCategoryAdapter.clear()
                        selSubCatIndex = -1
                        for (subCategory in subCategoryList) {
                            subCategoryAdapter.add(subCategory.name)
                        }
                        selectSubCategoryToAdapter()
                        emit(Resource.success(subCategoryList))
                    } else {
                        val error = subCatByCatIdResponse.error
                        if (error != null) {
                            emit(Resource.error(null, error.errorMessage ?: ""))
                        }
                    }
                } else {
                    // get sub category from category
                    val subCatBySubCatIdResponse =
                        masterRepository.getSubCategoryBySubCatId(catId, subCatId)
                    if (subCatBySubCatIdResponse.data != null) {
                        subCategoryList = subCatBySubCatIdResponse.data
                        categoryAdapter.clear()
                        for (subCategory in subCategoryList) {
                            subCategoryAdapter.add(subCategory.name)
                        }
                        selectSubCategoryToAdapter()
                        emit(Resource.success(subCategoryList))
                    } else {
                        val error = subCatBySubCatIdResponse.error
                        if (error != null) {
                            emit(Resource.error(null, error.errorMessage ?: ""))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(null, e.message ?: "Exception occurred"))
        }
    }

    fun validateNewSubCategory(): Boolean {
        Log.d(TAG, "Validate Sub Category : ${newSubCategory}")

        isButtonClicked = true
        var isSubCatDataValid = false
//        if (category.id.isEmpty()) {
//            isCatDataValid = false
//            errCatId.value = Constants.ERR_CAT_ID
//        } else {
        if (newSubCategory.name.isEmpty()) {
            isSubCatDataValid = false
            errNewSubCat.value = ERR_SUB_CAT_NAME
        } else if (newSubCategory.catId <= 0) {
            isSubCatDataValid = false
            errCat.value = ERR_SELECT_CAT
        } else {
            isSubCatDataValid = true
            val networkAvailblePair = mContext.isNetworkAvailable
            if (networkAvailblePair.first) {
                saveSubCategory()
            } else {
                mContext.showToast(networkAvailblePair.second)
            }
        }
//        }
        return isSubCatDataValid
    }

    fun saveSubCategory() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            saveSubCatResponse.value = Resource.loading(null)
        }
        try {
            var saveSubCategoryResponse = BaseResponse<CommonResponse>()
            if (isEdit) {
                saveSubCategoryResponse = masterRepository.updateSubCategory(newSubCategory)
            } else {
                saveSubCategoryResponse = masterRepository.saveSubCategory(newSubCategory)
            }

            if (saveSubCategoryResponse.data != null) {
                withContext(Dispatchers.Main) {
                    saveSubCatResponse.value = Resource.success(saveSubCategoryResponse.data!!)
                }
            } else {
                val error = saveSubCategoryResponse.error
                if (error != null) {
                    withContext((Dispatchers.Main)) {
                        saveSubCatResponse.value = Resource.error(null, error.errorMessage!!)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                saveSubCatResponse.value = Resource.error(null, getExceptionMessage(e))
            }

        }
    }

    private fun selectCategoryToAdapter() {
        selCatIndex =
            categoryList.indexOfFirst { category -> category.id!!.equals(this.category.id) }
        if (selCatIndex >= 0)
            setCategory(categoryList[selCatIndex])
    }

    private fun selectSubCategoryToAdapter() {
        selSubCatIndex =
            subCategoryList.indexOfFirst { subCategory -> subCategory.id!!.equals(this.subCategory.subCatId) }
        if (selSubCatIndex >= 0)
            setSubCategory(subCategoryList[selSubCatIndex])

    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {

    }

}