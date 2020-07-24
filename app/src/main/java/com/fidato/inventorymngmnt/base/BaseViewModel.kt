package com.fidato.inventorymngmnt.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.fidato.inventorymngmnt.constants.Constants
import java.net.ConnectException

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    fun getExceptionMessage(e: Exception): String {
        var errMsg = ""
        if (e is ConnectException) {
            errMsg = Constants.connect_exception
        } else {
            if (!e.localizedMessage.isNullOrEmpty())
                errMsg = e.localizedMessage
            else
                errMsg = Constants.something_wrong
        }
        return errMsg
    }

    open fun onTextChanged(textChangeComponent: TextChangeComponent) {

    }

}

enum class TextChangeComponent {
    PRODUCT_NAME,
    PRODUCT_DESC,
    PRODUCT_SUB_CAT,
    PRODUCT_PRICE,
    PRODUCT_QUANTITY,
    PRODUCT_COLOR,
    PRODUCT_SIZE,
    CUSTOMER_NAME,
    CUSTOMER_EMIAL,
    CUSTOMER_MOBILE,
    CUSTOMER_PASSWORD,
    SUPPLIER_NAME,
    SUPPLIER_EMAIL,
    SUPPLIER_PASSWORD
}