package com.fidato.inventorymngmnt.ui.products.viewmodel

import android.app.Application
import androidx.lifecycle.liveData
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.products.adapter.ProductAdapter
import com.fidato.inventorymngmnt.utility.Resource
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

}