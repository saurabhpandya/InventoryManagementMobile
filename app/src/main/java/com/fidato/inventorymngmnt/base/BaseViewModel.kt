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

}