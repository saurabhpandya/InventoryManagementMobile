package com.fidato.inventorymanagement.data.master

import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_CATEGORY_DELETE
import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_CATEGORY_GET
import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_CATEGORY_SAVE
import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_CATEGORY_UPDATE
import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_MASTER_CATEGORY
import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_MASTER_SUB_CATEGORY
import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_DELETE
import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_GET
import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_SAVE
import com.fidato.inventorymanagement.constants.ApiConstants.Companion.ENDPOINT_SUB_CATEGORY_UPDATE
import com.fidato.inventorymanagement.data.model.BaseResponse
import com.fidato.inventorymanagement.data.model.CommonResponse
import com.fidato.inventorymanagement.data.model.master.Category
import com.fidato.inventorymanagement.data.model.master.SubCategory
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

    @GET(ENDPOINT_SUB_CATEGORY_SAVE)
    suspend fun saveSubCategory(@Body saveSubCategoryRequest: SubCategory): BaseResponse<CommonResponse>

    @GET(ENDPOINT_SUB_CATEGORY_DELETE)
    suspend fun deleteSubCategory(@Body deleteSubCategoryRequest: SubCategory): BaseResponse<CommonResponse>

    @GET(ENDPOINT_SUB_CATEGORY_UPDATE)
    suspend fun updateSubCategory(@Body updateSubCategoryRequest: SubCategory): BaseResponse<CommonResponse>


}