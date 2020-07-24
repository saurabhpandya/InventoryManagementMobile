package com.fidato.inventorymngmnt.data.supplier

import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUPPLIER
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUPPLIER_DELETE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUPPLIER_GET
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUPPLIER_SAVE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_SUPPLIER_UPDATE
import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.Supplier
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SupplierServices {

    @GET(ENDPOINT_SUPPLIER)
    suspend fun getAllSuppliers(): BaseResponse<ArrayList<Supplier>>

    @GET(ENDPOINT_SUPPLIER_GET)
    suspend fun getSupplierById(@Path("id") id: Int): BaseResponse<Supplier>

    @POST(ENDPOINT_SUPPLIER_SAVE)
    suspend fun saveSupplier(@Body saveCustomerRequest: Supplier): BaseResponse<CommonResponse>

    @POST(ENDPOINT_SUPPLIER_DELETE)
    suspend fun deleteSupplier(@Body deleteSupplierRequest: Supplier): BaseResponse<CommonResponse>

    @POST(ENDPOINT_SUPPLIER_UPDATE)
    suspend fun updateSupplier(@Body updateSupplierRequest: Supplier): BaseResponse<CommonResponse>
}