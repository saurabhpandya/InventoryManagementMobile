package com.fidato.inventorymngmnt.ui.products.viewmodel

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.data.model.master.SubCategory
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.products.adapter.AddProductVariantAdapter
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.SwipeHelper
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditProductViewModel(
    application: Application,
    val masterRepository: MasterRepository
) : BaseViewModel(application) {
    private val TAG: String = AddEditProductViewModel::class.java.canonicalName.toString()

    val mContext = application.applicationContext

    var errPrdctName = MutableLiveData<String>("")
    var errPrdctDesc = MutableLiveData<String>("")
    var errPrdctSubCat = MutableLiveData<String>("")

    var product = Product()

    var subCategories = ArrayList<SubCategory>()

    var isEdit = false

    lateinit var addProductVariantAdap: AddProductVariantAdapter
    var subCategoryAdapter =
        ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice)

    var selSubCatIndex = -1

    var saveProductResponse = MutableLiveData<Resource<CommonResponse>>()

    fun setBundleData(bundle: Bundle?) {
        if (bundle != null) {
            isEdit = bundle.getBoolean(Constants.BUNDLE_EDIT_PRODUCT, false)
            if (isEdit) {
                product = bundle.getParcelable<Product>(Constants.BUNDLE_PRODUCT)!!
            } else {
                product.subCatId = bundle.getInt(Constants.BUNDLE_SUB_CAT_ID, -1)
            }
        }
    }

    fun validateProductData() {
        Log.d(TAG, "Product: $product")
        if (product.name.isNullOrEmpty()) {
            errPrdctName.value = Constants.ERR_PRODUCT_NAME
        } else if (product.description.isNullOrEmpty()) {
            errPrdctDesc.value = Constants.ERR_PRODUCT_DESC
        } else if (selSubCatIndex == -1) {
            errPrdctSubCat.value = Constants.ERR_SELECT_SUB_CAT
        } else {
            saveProduct()
        }
    }

    private fun saveProduct() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            saveProductResponse.value = Resource.loading(null)
        }
        try {
            var saveProductBaseResponse = BaseResponse<CommonResponse>()
            if (isEdit) {
                saveProductBaseResponse = masterRepository.updateProduct(product)
            } else {
                saveProductBaseResponse = masterRepository.saveProduct(product)
            }

            if (saveProductBaseResponse.data != null) {
                withContext(Dispatchers.Main) {
                    saveProductResponse.value = Resource.success(saveProductBaseResponse.data!!)
                }
            } else {
                val error = saveProductBaseResponse.error
                if (error != null) {
                    withContext((Dispatchers.Main)) {
                        saveProductResponse.value = Resource.error(null, error.errorMessage!!)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                saveProductResponse.value = Resource.error(null, e.localizedMessage)
            }

        }
    }

    fun textChanged(textChangeComponent: TextChangeComponent) {
        when (textChangeComponent) {
            TextChangeComponent.PRODUCT_NAME -> {
                errPrdctName.postValue("")
            }
            TextChangeComponent.PRODUCT_DESC -> {
                errPrdctDesc.postValue("")
            }
            TextChangeComponent.PRODUCT_SUB_CAT -> {
                errPrdctSubCat.postValue("")
            }
        }
    }

    fun getProduct() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            if (isEdit && product.id != null) {
                val networkConnectPair = mContext.isNetworkAvailable
                if (networkConnectPair.first) {
                    val productBaseResponse = masterRepository.getProductById(product.id!!)
                    if (productBaseResponse.data != null) {
                        product = productBaseResponse.data
                        emit(Resource.success(productBaseResponse.data))
                    } else if (productBaseResponse.error != null) {
                        emit(
                            Resource.error(
                                null,
                                productBaseResponse.error.errorMessage ?: Constants.something_wrong
                            )
                        )
                    }

                } else {
                    emit(Resource.error(null, networkConnectPair.second))
                }

            } else {
                emit(Resource.success(product))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(null, getExceptionMessage(e)))
        }
    }

    fun getSwipeHelper(addProductUnderLayBtnClickLister: CategoryUnderlayButtonClickListner) =
        object : SwipeHelper(mContext) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>
            ) {
                underlayButtons.add(
                    UnderlayButton("Edit",
                        0,
                        ContextCompat.getColor(mContext, R.color.btn_edit),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                addProductUnderLayBtnClickLister.editUnderlayClicked(pos)
                            }
                        })
                )

                underlayButtons.add(
                    UnderlayButton("Delete",
                        0,
                        ContextCompat.getColor(mContext, R.color.btn_delete),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                addProductUnderLayBtnClickLister.deleteUnderlayClicked(pos)
                            }
                        })
                )
            }
        }

    fun getSubCategory() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val subCategoriesBaseResponse = masterRepository.getSubCategoryLeafs()

            if (subCategoriesBaseResponse.data != null) {
                val subCategories = ArrayList<SubCategory>()
                subCategories.add(
                    SubCategory(
                        id = 0,
                        subCatId = 0,
                        name = "None",
                        catId = 0
                    )
                )
                subCategories.addAll(subCategoriesBaseResponse.data)
                for ((index, subCategory) in subCategories.withIndex()) {
                    subCategoryAdapter.add(subCategory.name)
                    if (product.subCatId?.equals(subCategory.id)!!)
                        selSubCatIndex = index

                }
                emit(Resource.success(subCategories))
            } else if (subCategoriesBaseResponse.error != null) {
                val error = subCategoriesBaseResponse.error
                emit(Resource.error(null, error.errorMessage!!))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(null, getExceptionMessage(e)))
        }
    }

}

enum class TextChangeComponent {
    PRODUCT_NAME,
    PRODUCT_DESC,
    PRODUCT_SUB_CAT,
    PRODUCT_PRICE,
    PRODUCT_QUANTITY,
    PRODUCT_COLOR,
    PRODUCT_SIZE
}