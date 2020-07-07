package com.fidato.inventorymngmnt.data.repository

import com.fidato.inventorymngmnt.data.master.MasterNetworkDataProvider
import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.data.model.master.SubCategory

class MasterRepository(private val masterDataProvider: MasterNetworkDataProvider) {
    // Category
    suspend fun getCategory() = masterDataProvider.getCategory()

    suspend fun getCategoryById(id: Int) =
        masterDataProvider.getCategoryById(id)

    suspend fun saveCategory(saveCatRequest: Category) =
        masterDataProvider.saveCategory(saveCatRequest)

    suspend fun updateCategory(updateCatRequest: Category) =
        masterDataProvider.updateCategory(updateCatRequest)

    suspend fun deleteCategory(deleteCatRequest: Category) =
        masterDataProvider.deleteCategory(deleteCatRequest)

    // Sub Category
    suspend fun getSubCategory() = masterDataProvider.getSubCategory()

    suspend fun getSubCategoryById(id: Int) =
        masterDataProvider.getSubCategoryById(id)

    suspend fun getSubCategoryByCatId(catId: Int) =
        masterDataProvider.getSubCategoryByCatId(catId)

    suspend fun getSubCategoryBySubCatId(catId: Int, subCatId: Int) =
        masterDataProvider.getSubCategoryBySubCatId(catId, subCatId)

    suspend fun saveSubCategory(saveSubCatRequest: SubCategory) =
        masterDataProvider.saveSubCategory(saveSubCatRequest)

    suspend fun updateSubCategory(updateSubCatRequest: SubCategory) =
        masterDataProvider.updateSubCategory(updateSubCatRequest)

    suspend fun deleteSubCategory(deleteSubCatRequest: SubCategory) =
        masterDataProvider.deleteSubCategory(deleteSubCatRequest)

    // Product
    suspend fun getAllProducts(): BaseResponse<ArrayList<Product>> =
        masterDataProvider.getAllProducts()

    suspend fun getProductById(id: Int): BaseResponse<Product> =
        masterDataProvider.getProductById(id)

    suspend fun getProductBySubCatId(subCatId: Int): BaseResponse<ArrayList<Product>> =
        masterDataProvider.getProductBySubCatId(subCatId)

    suspend fun saveProduct(saveProductRequest: Product): BaseResponse<CommonResponse> =
        masterDataProvider.saveProduct(saveProductRequest)

    suspend fun deleteProduct(deleteProductRequest: Product): BaseResponse<CommonResponse> =
        masterDataProvider.deleteProduct(deleteProductRequest)

    suspend fun updateProduct(updateProductRequest: Product): BaseResponse<CommonResponse> =
        masterDataProvider.updateProduct(updateProductRequest)

}