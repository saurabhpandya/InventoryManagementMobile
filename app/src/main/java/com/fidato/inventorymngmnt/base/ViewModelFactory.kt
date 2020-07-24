package com.fidato.inventorymngmnt.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fidato.inventorymngmnt.data.customer.CustomerNetworkDataProvider
import com.fidato.inventorymngmnt.data.customer.CustomerRepository
import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.repository.MasterRepository
import com.fidato.inventorymngmnt.data.supplier.SupplierNetworkDataProvider
import com.fidato.inventorymngmnt.data.supplier.SupplierRepository
import com.fidato.inventorymngmnt.ui.customer.viewmodel.AddEditCustomerViewModel
import com.fidato.inventorymngmnt.ui.customer.viewmodel.CustomerViewModel
import com.fidato.inventorymngmnt.ui.dashboard.viewmodel.DashboardViewModel
import com.fidato.inventorymngmnt.ui.master.viewmodel.AddEditCategoryViewModel
import com.fidato.inventorymngmnt.ui.master.viewmodel.AddEditSubCategoryViewModel
import com.fidato.inventorymngmnt.ui.master.viewmodel.CategoryViewModel
import com.fidato.inventorymngmnt.ui.master.viewmodel.SubCategoryViewModel
import com.fidato.inventorymngmnt.ui.products.viewmodel.AddEditProductVariantViewModel
import com.fidato.inventorymngmnt.ui.products.viewmodel.AddEditProductViewModel
import com.fidato.inventorymngmnt.ui.products.viewmodel.ProductDetailsViewModel
import com.fidato.inventorymngmnt.ui.products.viewmodel.ProductViewModel
import com.fidato.inventorymngmnt.ui.supplier.viewmodel.AddEditSupplierViewModel
import com.fidato.inventorymngmnt.ui.supplier.viewmodel.SupplierViewModel

class ViewModelFactory<T>(private val dataProvider: T, private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AddEditSupplierViewModel::class.java)) {
            return AddEditSupplierViewModel(
                application,
                SupplierRepository(
                    dataProvider as SupplierNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(SupplierViewModel::class.java)) {
            return SupplierViewModel(
                application,
                SupplierRepository(
                    dataProvider as SupplierNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(AddEditCustomerViewModel::class.java)) {
            return AddEditCustomerViewModel(
                application,
                CustomerRepository(
                    dataProvider as CustomerNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            return CustomerViewModel(
                application,
                CustomerRepository(
                    dataProvider as CustomerNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(AddEditProductVariantViewModel::class.java)) {
            return AddEditProductVariantViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(AddEditProductViewModel::class.java)) {
            return AddEditProductViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(AddEditSubCategoryViewModel::class.java)) {
            return AddEditSubCategoryViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(AddEditCategoryViewModel::class.java)) {
            return AddEditCategoryViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(SubCategoryViewModel::class.java)) {
            return SubCategoryViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        } else if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(
                application,
                MasterRepository(
                    dataProvider as MasterNetworkDataProvider
                )
            ) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}