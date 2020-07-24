package com.fidato.inventorymngmnt.ui.supplier.viewmodel

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.base.TextChangeComponent
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.Customer
import com.fidato.inventorymngmnt.data.model.Supplier
import com.fidato.inventorymngmnt.data.supplier.SupplierRepository
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import com.fidato.inventorymngmnt.utility.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditSupplierViewModel(
    application: Application,
    val supplierRepository: SupplierRepository
) : BaseViewModel(application) {

    private val TAG: String = this::class.java.canonicalName.toString()

    private var mContext = application.applicationContext

    var supplier: Supplier = Supplier()

    var isEdit: Boolean = false

    var errSupName = MutableLiveData<String>("")
    var errSupEmail = MutableLiveData<String>("")
    var errSupPassword = MutableLiveData<String>("")

    var saveSupplierResponse = MutableLiveData<Resource<CommonResponse>>()

    fun setBundleData(bundle: Bundle?) {
        if (bundle != null) {
            isEdit = bundle.getBoolean(Constants.BUNDLE_EDIT_SUPPLIER)

            if (bundle.getParcelable<Customer>(Constants.BUNDLE_SUPPLIER) != null) {
                supplier = bundle.getParcelable<Supplier>(Constants.BUNDLE_SUPPLIER)!!
            }
        }
    }

    fun validateSupplier() {
        Log.d(TAG, "Save Supplier : $supplier")
        if (!supplier.name.isEmpty()) {
            if (!supplier.email.isEmpty()) {
                if (!supplier.password.isEmpty()) {
                    val networkPair = mContext.isNetworkAvailable
                    if (networkPair.first) {
                        saveSupplier()
                    } else {
                        mContext.showToast(networkPair.second)
                    }
                } else {
                    errSupPassword.postValue(Constants.ERR_SUPPLIER_PASSWORD)
                }
            } else {
                errSupEmail.postValue(Constants.ERR_SUPPLIER_EMAIL)
            }
        } else {
            errSupName.postValue(Constants.ERR_SUPPLIER_NAME)
        }
    }

    private fun saveSupplier() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            saveSupplierResponse.value = Resource.loading(null)
        }
        try {
            var saveSupplierBaseResponse = BaseResponse<CommonResponse>()
            if (isEdit)
                saveSupplierBaseResponse = supplierRepository.updateSupplier(supplier)
            else
                saveSupplierBaseResponse = supplierRepository.saveSupplier(supplier)
            if (saveSupplierBaseResponse.data != null) {
                withContext(Dispatchers.Main) {
                    saveSupplierResponse.value = Resource.success(saveSupplierBaseResponse.data!!)
                }
            } else if (saveSupplierBaseResponse.error != null) {
                withContext(Dispatchers.Main) {
                    saveSupplierResponse.value =
                        Resource.error(null, saveSupplierBaseResponse.error?.errorMessage!!)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                saveSupplierResponse.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {
        when (textChangeComponent) {
            TextChangeComponent.SUPPLIER_NAME -> {
                errSupName.postValue("")
            }
            TextChangeComponent.SUPPLIER_EMAIL -> {
                errSupEmail.postValue("")
            }
            TextChangeComponent.SUPPLIER_PASSWORD -> {
                errSupPassword.postValue("")
            }
        }
    }


}