package com.fidato.inventorymngmnt.ui.customer.viewmodel

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.base.TextChangeComponent
import com.fidato.inventorymngmnt.data.customer.CustomerRepository
import com.fidato.inventorymngmnt.data.model.Customer
import com.fidato.inventorymngmnt.ui.customer.adapter.CustomerAdapter
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.SwipeHelper
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import kotlinx.coroutines.Dispatchers

class CustomerViewModel(
    application: Application,
    val customerRepository: CustomerRepository
) : BaseViewModel(application) {

    private val TAG = this::class.java.canonicalName

    private val mContext = application.applicationContext

    lateinit var customerAdapter: CustomerAdapter
    private var arylstCustomer = ArrayList<Customer>()

    fun getCustomerList() = arylstCustomer

    fun getSwipeHelper(catUnderLayBtnClickLister: CategoryUnderlayButtonClickListner) =
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
                                catUnderLayBtnClickLister.editUnderlayClicked(pos)
                            }
                        })
                )

                underlayButtons.add(
                    UnderlayButton("Delete",
                        0,
                        ContextCompat.getColor(mContext, R.color.btn_delete),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                catUnderLayBtnClickLister.deleteUnderlayClicked(pos)
                            }
                        })
                )
            }
        }

    fun getCustomers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val networkValidatorPair = mContext.isNetworkAvailable
        if (networkValidatorPair.first) {
            try {
                val customerBaseResponse = customerRepository.getCustomer()
                if (customerBaseResponse.data != null) {
                    arylstCustomer = customerBaseResponse.data
                    emit(Resource.success(arylstCustomer))
                } else {
                    emit(Resource.error(null, customerBaseResponse.error?.errorMessage!!))
                }

            } catch (e: Exception) {
                emit(Resource.error(null, getExceptionMessage(e)))
            }
        } else {
            emit(Resource.error(null, networkValidatorPair.second))
        }

    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {

    }

}