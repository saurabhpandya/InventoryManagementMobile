package com.fidato.inventorymngmnt.ui.products.viewmodel

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.RecyclerView
import com.fidato.inventorymngmnt.R
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.base.TextChangeComponent
import com.fidato.inventorymngmnt.constants.Constants
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.products.adapter.ProductAdapter
import com.fidato.inventorymngmnt.utility.CategoryUnderlayButtonClickListner
import com.fidato.inventorymngmnt.utility.Resource
import com.fidato.inventorymngmnt.utility.SwipeHelper
import kotlinx.coroutines.Dispatchers

class ProductViewModel(
    application: Application,
    val masterRepository: MasterRepository
) : BaseViewModel(application) {

    private val TAG = ProductViewModel::class.java.canonicalName

    private val context = application.applicationContext

    var arylstProduct: ArrayList<Product>

    lateinit var productAdapter: ProductAdapter

    var subCatId: Int = 0
    var productId: Int = 0

    init {
        arylstProduct = ArrayList<Product>()
    }

    fun getSwipeHelper(productUnderLayBtnClickLister: CategoryUnderlayButtonClickListner) =
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
                                productUnderLayBtnClickLister.editUnderlayClicked(pos)
                            }
                        })
                )

                underlayButtons.add(
                    UnderlayButton("Delete",
                        0,
                        ContextCompat.getColor(context, R.color.btn_delete),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                productUnderLayBtnClickLister.deleteUnderlayClicked(pos)
                            }
                        })
                )
            }
        }

    fun getBundleToEditProduct(product: Product): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(Constants.BUNDLE_PRODUCT, product)
        return bundle
    }

    fun getProductsBySubCatId() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val productsBySubCatId = masterRepository.getProductBySubCatId(subCatId)
            if (productsBySubCatId.data != null) {
                emit(Resource.success(productsBySubCatId.data))
            } else {
                emit(Resource.error(null, productsBySubCatId.error?.errorMessage ?: ""))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(null, e.localizedMessage))
        }
    }

    fun setProductData(arylstProduct: ArrayList<Product>) {
        this.arylstProduct = arylstProduct
        productAdapter.setProduct(this.arylstProduct)
    }

    fun deleteProduct(position: Int) {
        val product = arylstProduct[position]
        Log.d(TAG, "Delete Underlay Button clicked for : ${product.name}")
    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {

    }

}