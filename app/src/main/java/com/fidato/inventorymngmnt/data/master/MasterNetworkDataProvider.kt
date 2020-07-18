package com.fidato.inventorymngmnt.data.master

import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.data.model.master.ProductVarient
import com.fidato.inventorymngmnt.data.model.master.SubCategory

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

    suspend fun getSubCategoryLeafs() = masterService.getAllSubCategoryLeafs()

    suspend fun getSubCategoryById(id: Int) = masterService.getSubCategoryById(id)

    suspend fun getSubCategoryByCatId(catId: Int) = masterService.getSubCategoryByCatId(catId)

    suspend fun getSubCategoryBySubCatId(catId: Int, subCatId: Int) =
        masterService.getSubCategoryBySubCatId(catId, subCatId)

    suspend fun saveSubCategory(saveSubCatRequest: SubCategory) =
        masterService.saveSubCategory(saveSubCatRequest)

    suspend fun updateSubCategory(updateSubCatRequest: SubCategory) =
        masterService.updateSubCategory(updateSubCatRequest)

    suspend fun deleteSubCategory(deleteSubCatRequest: SubCategory) =
        masterService.deleteSubCategory(deleteSubCatRequest)

    // Product
    suspend fun getAllProducts(): BaseResponse<ArrayList<Product>> = masterService.getAllProducts()

    suspend fun getProductById(id: Int): BaseResponse<Product> =
        masterService.getProductById(id)

    suspend fun getProductBySubCatId(subCatId: Int): BaseResponse<ArrayList<Product>> =
        masterService.getProductBySubCatId(subCatId)

    suspend fun saveProduct(saveProductRequest: Product): BaseResponse<CommonResponse> =
        masterService.saveProduct(saveProductRequest)

    suspend fun deleteProduct(deleteProductRequest: Product): BaseResponse<CommonResponse> =
        masterService.deleteProduct(deleteProductRequest)

    suspend fun updateProduct(updateProductRequest: Product): BaseResponse<CommonResponse> =
        masterService.updateProduct(updateProductRequest)

    // Product Variant
    suspend fun getAllProductVariants(): BaseResponse<ArrayList<ProductVarient>> =
        masterService.getAllProductVariants()

    suspend fun getProductVariantById(id: Int): BaseResponse<ProductVarient> =
        masterService.getProductVariantById(id)

    suspend fun getProductVariantByProductId(productId: Int): BaseResponse<ArrayList<ProductVarient>> =
        masterService.getProductVariantByProductId(productId)

    suspend fun saveProductVariant(saveProductRequest: ProductVarient): BaseResponse<CommonResponse> =
        masterService.saveProductVariant(saveProductRequest)

    suspend fun deleteProductVariant(deleteProductRequest: ProductVarient): BaseResponse<CommonResponse> =
        masterService.deleteProductVariant(deleteProductRequest)

    suspend fun updateProductVariant(updateProductRequest: ProductVarient): BaseResponse<CommonResponse> =
        masterService.updateProductVariant(updateProductRequest)

}