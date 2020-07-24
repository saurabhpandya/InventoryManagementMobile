package com.fidato.inventorymngmnt.ui.customer.viewmodel

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.base.TextChangeComponent
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.data.customer.CustomerRepository
import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.Customer
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import com.fidato.inventorymngmnt.utility.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditCustomerViewModel(
    application: Application,
    val customerRepository: CustomerRepository
) : BaseViewModel(application) {

    private val TAG: String = this::class.java.canonicalName.toString()
    private var mContext = application.applicationContext

    var customer: Customer = Customer()

    var isEdit: Boolean = false

    var errCustName = MutableLiveData<String>("")
    var errCustEmail = MutableLiveData<String>("")
    var errCustMobile = MutableLiveData<String>("")
    var errCustPassword = MutableLiveData<String>("")

    var saveCustomerResponse = MutableLiveData<Resource<CommonResponse>>()

    fun setBundleData(bundle: Bundle?) {
        if (bundle != null) {
            isEdit = bundle.getBoolean(Constants.BUNDLE_EDIT_CUSTOMER)

            if (bundle.getParcelable<Customer>(Constants.BUNDLE_CUSTOMER) != null) {
                customer = bundle.getParcelable<Customer>(Constants.BUNDLE_CUSTOMER)!!
            }
        }
    }

    fun validateCustomer() {
        Log.d(TAG, "Save Customer : $customer")
        if (!customer.name.isEmpty()) {
            if (!customer.email.isEmpty()) {
                if (!customer.mobile.isEmpty()) {
                    if (!customer.password.isEmpty()) {
                        val networkPair = mContext.isNetworkAvailable
                        if (networkPair.first) {
                            saveCustomer()
                        } else {
                            mContext.showToast(networkPair.second)
                        }

                    } else {
                        errCustPassword.postValue(Constants.ERR_CUSTOMER_PASSWORD)
                    }
                } else {
                    errCustMobile.postValue(Constants.ERR_CUSTOMER_MOBILE)
                }
            } else {
                errCustEmail.postValue(Constants.ERR_CUSTOMER_EMAIL)
            }
        } else {
            errCustName.postValue(Constants.ERR_CUSTOMER_NAME)
        }
    }

    private fun saveCustomer() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            saveCustomerResponse.value = Resource.loading(null)
        }
        try {
            var saveCustomerBaseResponse = BaseResponse<CommonResponse>()
            if (isEdit)
                saveCustomerBaseResponse = customerRepository.updateCustomer(customer)
            else
                saveCustomerBaseResponse = customerRepository.saveCustomer(customer)
            if (saveCustomerBaseResponse.data != null) {
                withContext(Dispatchers.Main) {
                    saveCustomerResponse.value = Resource.success(saveCustomerBaseResponse.data!!)
                }
            } else if (saveCustomerBaseResponse.error != null) {
                withContext(Dispatchers.Main) {
                    saveCustomerResponse.value =
                        Resource.error(null, saveCustomerBaseResponse.error?.errorMessage!!)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                saveCustomerResponse.value = Resource.error(null, e.localizedMessage)
            }
        }
    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {
        when (textChangeComponent) {
            TextChangeComponent.CUSTOMER_NAME -> {
                errCustName.postValue("")
            }
            TextChangeComponent.CUSTOMER_EMIAL -> {
                errCustEmail.postValue("")
            }
            TextChangeComponent.CUSTOMER_MOBILE -> {
                errCustMobile.postValue("")
            }
            TextChangeComponent.CUSTOMER_PASSWORD -> {
                errCustPassword.postValue("")
            }
        }
    }


}