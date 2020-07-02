package com.fidato.inventorymanagement.data.master

import com.fidato.inventorymanagement.data.model.master.Category
import com.fidato.inventorymanagement.data.model.master.SubCategory

class MasterNetworkDataProvider(private val masterService: MasterService) {

    //  Category
    suspend fun getCategory() = masterService.getAllCategory()

    suspend fun getCategoryById(id: Int) = masterService.getCategoryById(id)

    suspend fun saveCategory(saveCatRequest: Category) =
        masterService.saveCategory(saveCatRequest)

    suspend fun updateCategory(updateCatRequest: Category) =
        masterService.updateCategory(updateCatRequest)

    suspend fun deleteCategory(deleteCatRequest: Category) =
        masterService.deleteCategory(deleteCatRequest)

    //  Sub Category
    suspend fun getSubCategory() = masterService.getAllSubCategory()

    suspend fun getSubCategoryById(id: Int) = masterService.getSubCategoryById(id)

    suspend fun saveSubCategory(saveSubCatRequest: SubCategory) =
        masterService.saveSubCategory(saveSubCatRequest)

    suspend fun updateSubCategory(updateSubCatRequest: SubCategory) =
        masterService.updateSubCategory(updateSubCatRequest)

    suspend fun deleteSubCategory(deleteSubCatRequest: SubCategory) =
        masterService.deleteSubCategory(deleteSubCatRequest)

}