package com.fidato.inventorymngmnt.ui.products.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.liveData
import com.fidato.inventorymngmnt.base.BaseViewModel
import com.fidato.inventorymngmnt.base.TextChangeComponent
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

    var product: Product = Product()

    var arylstProductSizeVarient: ArrayList<ProductVarient> = ArrayList<ProductVarient>()
    var arylstProductColorVarient: ArrayList<ProductVarient> = ArrayList<ProductVarient>()

    lateinit var productSizeVarientAdapter: ProductVariantAdapter<ProductVarient>
    lateinit var productColorVarientAdapter: ProductVariantAdapter<ProductVarient>

    var productId: Int = 0

    lateinit var productVariantMapBySize: Map<String, List<ProductVarient>>

    fun getProductsById() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val productsBySubCatId = masterRepository.getProductById(productId)
            if (productsBySubCatId.data != null) {
                product = productsBySubCatId.data
                getProductVariantMapByColor()
                emit(Resource.success(product))
            } else if (productsBySubCatId.error != null) {
                emit(Resource.error(null, productsBySubCatId.error.errorMessage ?: ""))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(null, getExceptionMessage(e)))
        }
    }

    fun getProductVariantMapByColor() {
        var productVariantsList = product.productVariantMapping
        if (!productVariantsList.isNullOrEmpty()) {
            productVariantMapBySize = productVariantsList.groupBy {
                it.size!!
            }
            Log.d(TAG, "productVariantMapBySize:: ${productVariantMapBySize}")

        }
    }

    fun setProductVarientData(arylstProduct: ArrayList<Product>) {

    }

    override fun onTextChanged(textChangeComponent: TextChangeComponent) {

    }

}