package com.fidato.inventorymngmnt.ui.master.viewmodel

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.constants.Constants.Companion.BUNDLE_SUB_CAT
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.master.SubCategory
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.master.adapter.SubCategoryAdapter
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.SwipeHelper
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubCategoryViewModel(
    application: Application,
    private val masterRepo: MasterRepository
) : BaseViewModel(application) {

    private val TAG = SubCategoryViewModel::class.java.canonicalName

    private val context = application.applicationContext

    var arylstSubCategory: ArrayList<SubCategory>

    lateinit var subCategoryAdapter: SubCategoryAdapter

    var deleteSubCatResponse = MutableLiveData<Resource<CommonResponse>>()

    var catId: Int = -1
    var catName: String = ""
    var subCatId: Int = -1

    init {
        arylstSubCategory = ArrayList<SubCategory>()
    }

    fun getSubCategoryBundle(subCatListPos: Int) = bundleOf(
        BUNDLE_SUB_CAT to arylstSubCategory.get(subCatListPos)
    )

    fun getSubCategoryByCatId(catId: Int) = liveData(Dispatchers.IO) {
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

    fun getSwipeHelper(subCatUnderLayBtnClickLister: CategoryUnderlayButtonClickListner) =
        object : SwipeHelper(context) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>
            ) {
                underlayButtons.add(
                    UnderlayButton("Edit",
                        0,
                        ContextCompat.getColor(context, R.color.btn_edit),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                subCatUnderLayBtnClickLister.editUnderlayClicked(pos)
                            }
                        })
                )

                underlayButtons.add(
                    UnderlayButton("Delete",
                        0,
                        ContextCompat.getColor(context, R.color.btn_delete),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                subCatUnderLayBtnClickLister.deleteUnderlayClicked(pos)
                            }
                        })
                )
            }
        }

    fun deleteSubCategory(deleteSubCatRequest: SubCategory) =
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                deleteSubCatResponse.value = Resource.loading(null)
            }
            try {

                val deleteSubCategoryResponse = masterRepo.deleteSubCategory(deleteSubCatRequest)

                if (deleteSubCategoryResponse.data != null) {
                    withContext(Dispatchers.Main) {
                        deleteSubCatResponse.value =
                            Resource.success(deleteSubCategoryResponse.data)
                    }
                } else {
                    val error = deleteSubCategoryResponse.error
                    if (error != null) {
                        withContext((Dispatchers.Main)) {
                            deleteSubCatResponse.value = Resource.error(null, error.errorMessage!!)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    deleteSubCatResponse.value = Resource.error(null, e.localizedMessage)
                }

            }
        }

}