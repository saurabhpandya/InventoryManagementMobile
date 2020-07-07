package com.fidato.inventorymngmnt.ui.products.viewmodel

import android.app.Application
import androidx.lifecycle.liveData
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.data.model.master.ProductVarient
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.ui.products.adapter.ProductVariantAdapter
import com.fidato.inventorymngmnt.utility.Resource
import kotlinx.coroutines.Dispatchers

class ProductDetailsViewModel(
    application: Application,
    val masterRepository: MasterRepository
) : BaseViewModel(application) {

    private val TAG = ProductDetailsViewModel::class.java.canonicalName

    private val context = application.applicationContext

    var product: Product

    var arylstProductColorVarient: ArrayList<ProductVarient>

    lateinit var productColorVarientAdapter: ProductVariantAdapter<ProductVarient>

    var productId: Int = 0

    init {
        product = Product()
        arylstProductColorVarient = ArrayList<ProductVarient>()
    }

    fun getProductsById() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val productsBySubCatId = masterRepository.getProductById(productId)
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

    fun setProductVarientData(arylstProduct: ArrayList<Product>) {

    }

}