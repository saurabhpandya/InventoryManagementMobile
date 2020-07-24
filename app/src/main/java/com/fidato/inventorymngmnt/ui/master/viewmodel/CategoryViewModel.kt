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
import com.fidato.inventorymngmnt.base.TextChangeComponent
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.constants.Constants.Companion.connect_exception
import com.fidato.inventorymngmnt.constants.Constants.Companion.something_wrong
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.master.adapter.CategoryAdapter
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.SwipeHelper
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException

class CategoryViewModel(
    application: Application,
    private val masterRepo: MasterRepository
) : BaseViewModel(application) {

    private val TAG = CategoryViewModel::class.java.canonicalName

    private val context = application.applicationContext

    private var arylstCategory: ArrayList<Category>

    lateinit var categoryAdapter: CategoryAdapter

    var deleteCatResponse = MutableLiveData<Resource<CommonResponse>>()

    init {
        arylstCategory = ArrayList<Category>()
    }

    fun getCategoryList() = arylstCategory

    fun getCategoryBundle(catListPos: Int) = bundleOf(
        Constants.BUNDLE_CAT_ID to arylstCategory.get(catListPos).id,
        Constants.BUNDLE_CAT_NAME to arylstCategory.get(catListPos).name
    )

    fun getCategory() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val networkValidatorPair = context.isNetworkAvailable
        if (networkValidatorPair.first) {
            try {
                val categoryBaseResponse = masterRepo.getCategory()
                if (!categoryBaseResponse.data.isNullOrEmpty()) {
                    emit(Resource.success(categoryBaseResponse.data))
                } else {
                    if (categoryBaseResponse.error?.errorMessage != null
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

    fun getSwipeHelper(catUnderLayBtnClickLister: CategoryUnderlayButtonClickListner) =
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
                                catUnderLayBtnClickLister.editUnderlayClicked(pos)
                            }
                        })
                )

                underlayButtons.add(
                    UnderlayButton("Delete",
                        0,
                        ContextCompat.getColor(context, R.color.btn_delete),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                catUnderLayBtnClickLister.deleteUnderlayClicked(pos)
                            }
                        })
                )
            }
        }

    fun deleteCategory(deleteCatRequest: Category) = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            deleteCatResponse.value = Resource.loading(null)
        }
        try {

            val deleteCategoryResponse = masterRepo.deleteCategory(deleteCatRequest)

            if (deleteCategoryResponse.data != null) {
                withContext(Dispatchers.Main) {
                    deleteCatResponse.value = Resource.success(deleteCategoryResponse.data)
                }
            } else {
                val error = deleteCategoryResponse.error
                if (error != null) {
                    withContext((Dispatchers.Main)) {
                        deleteCatResponse.value = Resource.error(null, error.errorMessage!!)
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                deleteCatResponse.value = Resource.error(null, e.localizedMessage)
            }

        }
    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {

    }

}