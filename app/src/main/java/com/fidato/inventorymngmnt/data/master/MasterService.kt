package com.fidato.inventorymngmnt.data.master

import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_CATEGORY_DELETE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_CATEGORY_GET
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_CATEGORY_SAVE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_CATEGORY_UPDATE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_MASTER_CATEGORY
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_MASTER_PRODUCT
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_MASTER_SUB_CATEGORY
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_PRODUCT_BY_SUBCAT_ID
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_PRODUCT_DELETE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_PRODUCT_GET
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_PRODUCT_SAVE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_PRODUCT_UPDATE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_BY_CAT_ID
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_BY_SUBCAT_ID
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_DELETE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_GET
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_SAVE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_UPDATE
import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.master.Category
import com.fidato.inventorymngmnt.data.model.master.Product
import com.fidato.inventorymngmnt.data.model.master.SubCategory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface MasterService {

    // Category
    @GET(ENDPOINT_MASTER_CATEGORY)
    suspend fun getAllCategory(): BaseResponse<ArrayList<Category>>

    @GET(ENDPOINT_CATEGORY_GET)
    suspend fun getCategoryById(@Path("id") id: Int): BaseResponse<Category>

    @GET(ENDPOINT_CATEGORY_SAVE)
    suspend fun saveCategory(@Body saveCategoryRequest: Category): BaseResponse<CommonResponse>

    @GET(ENDPOINT_CATEGORY_DELETE)
    suspend fun deleteCategory(@Body deleteCategoryRequest: Category): BaseResponse<CommonResponse>

    @GET(ENDPOINT_CATEGORY_UPDATE)
    suspend fun updateCategory(@Body updateCategoryRequest: Category): BaseResponse<CommonResponse>

    // Sub Category
    @GET(ENDPOINT_MASTER_SUB_CATEGORY)
    suspend fun getAllSubCategory(): BaseResponse<ArrayList<SubCategory>>

    @GET(ENDPOINT_SUB_CATEGORY_GET)
    suspend fun getSubCategoryById(@Path("id") id: Int): BaseResponse<SubCategory>

    @GET(ENDPOINT_SUB_CATEGORY_BY_CAT_ID)
    suspend fun getSubCategoryByCatId(@Path("catId") catId: Int): BaseResponse<ArrayList<SubCategory>>

    @GET(ENDPOINT_SUB_CATEGORY_BY_SUBCAT_ID)
    suspend fun getSubCategoryBySubCatId(
        @Path("catId") catId: Int,
        @Path("subCatId") subCatId: Int
    ): BaseResponse<ArrayList<SubCategory>>

    @GET(ENDPOINT_SUB_CATEGORY_SAVE)
    suspend fun saveSubCategory(@Body saveSubCategoryRequest: SubCategory): BaseResponse<CommonResponse>

    @GET(ENDPOINT_SUB_CATEGORY_DELETE)
    suspend fun deleteSubCategory(@Body deleteSubCategoryRequest: SubCategory): BaseResponse<CommonResponse>

    @GET(ENDPOINT_SUB_CATEGORY_UPDATE)
    suspend fun updateSubCategory(@Body updateSubCategoryRequest: SubCategory): BaseResponse<CommonResponse>

    // Product
    @GET(ENDPOINT_MASTER_PRODUCT)
    suspend fun getAllProducts(): BaseResponse<ArrayList<Product>>

    @GET(ENDPOINT_PRODUCT_GET)
    suspend fun getProductById(@Path("id") id: Int): BaseResponse<Product>

    @GET(ENDPOINT_PRODUCT_BY_SUBCAT_ID)
    suspend fun getProductBySubCatId(@Path("subCatId") subCatId: Int): BaseResponse<ArrayList<Product>>

    @GET(ENDPOINT_PRODUCT_SAVE)
    suspend fun saveProduct(@Body saveProductRequest: Product): BaseResponse<CommonResponse>

    @GET(ENDPOINT_PRODUCT_DELETE)
    suspend fun deleteProduct(@Body deleteProductRequest: Product): BaseResponse<CommonResponse>

    @GET(ENDPOINT_PRODUCT_UPDATE)
    suspend fun updateProduct(@Body updateProductRequest: Product): BaseResponse<CommonResponse>


}