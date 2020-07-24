package com.fidato.inventorymngmnt.data.supplier

import com.fidato.inventorymngmnt.data.model.Supplier

class SupplierRepository(private val supplierDataProvider: SupplierNetworkDataProvider) {

    suspend fun getSupplier() = supplierDataProvider.getSupplier()

    suspend fun getSupplierById(id: Int) =
        supplierDataProvider.getSupplierById(id)

    suspend fun saveSupplier(saveSupplierRequest: Supplier) =
        supplierDataProvider.saveSupplier(saveSupplierRequest)

    suspend fun updateSupplier(updateSupplierRequest: Supplier) =
        supplierDataProvider.updateSupplier(updateSupplierRequest)

    suspend fun deleteSupplier(deleteSupplierRequest: Supplier) =
        supplierDataProvider.deleteSupplier(deleteSupplierRequest)
}