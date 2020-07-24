package com.fidato.inventorymngmnt.data.supplier

import com.fidato.inventorymngmnt.data.model.Supplier

class SupplierNetworkDataProvider(private val supplierService: SupplierServices) {

    suspend fun getSupplier() = supplierService.getAllSuppliers()

    suspend fun getSupplierById(id: Int) = supplierService.getSupplierById(id)

    suspend fun saveSupplier(saveCustomerRequest: Supplier) =
        supplierService.saveSupplier(saveCustomerRequest)

    suspend fun updateSupplier(updateSupplierRequest: Supplier) =
        supplierService.updateSupplier(updateSupplierRequest)

    suspend fun deleteSupplier(deleteSupplierRequest: Supplier) =
        supplierService.deleteSupplier(deleteSupplierRequest)
}