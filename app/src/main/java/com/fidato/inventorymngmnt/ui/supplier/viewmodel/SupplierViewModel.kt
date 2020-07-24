package com.fidato.inventorymngmnt.ui.supplier.viewmodel

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.data.model.Supplier
import com.fidato.inventorymngmnt.data.supplier.SupplierRepository
import com.fidato.inventorymngmnt.ui.supplier.adapter.SupplierAdapter
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.SwipeHelper
import com.fidato.inventorymngmnt.utility.isNetworkAvailable
import kotlinx.coroutines.Dispatchers

class SupplierViewModel(
    application: Application,
    val supplierRepository: SupplierRepository
) : BaseViewModel(application) {

    private val mContext = application.applicationContext

    lateinit var supplierAdapter: SupplierAdapter
    private var arylstSupplier = ArrayList<Supplier>()

    fun getSupplierList() = arylstSupplier

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

    fun getSuppliers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        val networkValidatorPair = mContext.isNetworkAvailable
        if (networkValidatorPair.first) {
            try {
                val supplierBaseResponse = supplierRepository.getSupplier()
                if (supplierBaseResponse.data != null) {
                    arylstSupplier = supplierBaseResponse.data
                    emit(Resource.success(arylstSupplier))
                } else {
                    emit(Resource.error(null, supplierBaseResponse.error?.errorMessage!!))
                }

            } catch (e: Exception) {
                emit(Resource.error(null, getExceptionMessage(e)))
            }
        } else {
            emit(Resource.error(null, networkValidatorPair.second))
        }

    }
}