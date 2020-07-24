package com.fidato.inventorymngmnt.data.customer

import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_CUSTOMER
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_CUSTOMER_DELETE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_CUSTOMER_GET
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_CUSTOMER_SAVE
import com.fidato.inventorymngmnt.constants.ApiConstants.Companion.ENDPOINT_CUSTOMER_UPDATE
import com.fidato.inventorymngmnt.data.model.BaseResponse
import com.fidato.inventorymngmnt.data.model.CommonResponse
import com.fidato.inventorymngmnt.data.model.Customer
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CustomerServices {
    
    @GET(ENDPOINT_CUSTOMER)
    suspend fun getAllCustomers(): BaseResponse<ArrayList<Customer>>

    @GET(ENDPOINT_CUSTOMER_GET)
    suspend fun getCustomerById(@Path("id") id: Int): BaseResponse<Customer>

    @POST(ENDPOINT_CUSTOMER_SAVE)
    suspend fun saveCustomer(@Body saveCustomerRequest: Customer): BaseResponse<CommonResponse>

    @POST(ENDPOINT_CUSTOMER_DELETE)
    suspend fun deleteCustomer(@Body deleteCustomerRequest: Customer): BaseResponse<CommonResponse>

    @POST(ENDPOINT_CUSTOMER_UPDATE)
    suspend fun updateCustomer(@Body updateCustomerRequest: Customer): BaseResponse<CommonResponse>
}