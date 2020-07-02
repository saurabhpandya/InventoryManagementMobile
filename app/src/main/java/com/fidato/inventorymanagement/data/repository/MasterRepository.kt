package com.fidato.inventorymanagement.data.repository

import com.fidato.inventorymanagement.data.master.MasterNetworkDataProvider
import com.fidato.inventorymanagement.data.model.master.Category

class MasterRepository(private val masterDataProvider: MasterNetworkDataProvider) {
    suspend fun getCategory() = masterDataProvider.getCategory()

    suspend fun getCategoryById(id: Int) =
        masterDataProvider.getCategoryById(id)

    suspend fun saveCategory(saveCatRequest: Category) =
        masterDataProvider.saveCategory(saveCatRequest)

    suspend fun updateCategory(updateCatRequest: Category) =
        masterDataProvider.updateCategory(updateCatRequest)

    suspend fun deleteCategory(deleteCatRequest: Category) =
        masterDataProvider.deleteCategory(deleteCatRequest)

}