package com.fidato.inventorymngmnt.ui.products.viewmodel

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.base.TextChangeComponent
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.master.ProductVarient
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.utility.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditProductVariantViewModel(
    application: Application,
    var masterRepository: MasterRepository
) : BaseViewModel(application) {
    private val TAG: String = AddEditProductVariantViewModel::class.java.canonicalName.toString()

    var productVariant = ProductVarient()
    var productName = ""
    var errPrdctPrice = MutableLiveData<String>("")
    var errPrdctQnty = MutableLiveData<String>("")
    var errPrdctColor = MutableLiveData<String>("")
    var errPrdctSize = MutableLiveData<String>("")

    var isEdit = false

    var saveProductVariantResponse = MutableLiveData<Resource<CommonResponse>>()

    fun getBundleData(bundle: Bundle) {
        isEdit = bundle.getBoolean(Constants.BUNDLE_EDIT_PRODUCT_VARIANT, false)
        productName = bundle.getString(Constants.BUNDLE_PRODUCT_NAME, "")
        if (isEdit) {
            productVariant =
                bundle.getParcelable<ProductVarient>(Constants.BUNDLE_PRODUCT_VARIANT)!!
            productVariant.productId = productVariant.productId
        } else {
            productVariant.productId = bundle.getInt(Constants.BUNDLE_PRODUCT_ID, -1)
        }
    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {
        Log.d(TAG, "textChanged::$textChangeComponent")
        when (textChangeComponent) {
            TextChangeComponent.PRODUCT_PRICE -> {
                errPrdctPrice.postValue("")
            }
            TextChangeComponent.PRODUCT_QUANTITY -> {
                errPrdctQnty.postValue("")
            }
            TextChangeComponent.PRODUCT_COLOR -> {
                errPrdctColor.postValue("")
            }
            TextChangeComponent.PRODUCT_SIZE -> {
                errPrdctSize.postValue("")
            }
        }
    }

    fun validateProductVariantData() {
        Log.d(TAG, "Product Variant: $productVariant")
        if (productVariant.price != null && productVariant.price!! > 0) {
            if (productVariant.quantity != null && productVariant.quantity!! > 0) {
                saveProductVariant()
            } else {
                errPrdctQnty.value = Constants.ERR_PRODUCT_VARIANT_QUANTITY
            }
        } else {
            errPrdctPrice.value = Constants.ERR_PRODUCT_VARIANT_PRICE
        }
    }

    private fun saveProductVariant() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            saveProductVariantResponse.value = Resource.loading(null)
        }
        try {

            var saveProductVariantBaseResponse =
                masterRepository.saveProductVariant(productVariant)

            if (saveProductVariantBaseResponse.data != null) {
                withContext(Dispatchers.Main) {
                    saveProductVariantResponse.value =
                        Resource.success(saveProductVariantBaseResponse.data!!)
                }
            } else {
                val error = saveProductVariantBaseResponse.error
                if (error != null) {
                    withContext((Dispatchers.Main)) {
                        saveProductVariantResponse.value =
                            Resource.error(null, error.errorMessage!!)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                saveProductVariantResponse.value = Resource.error(null, e.localizedMessage)
            }

        }
    }

}