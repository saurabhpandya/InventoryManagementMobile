package com.fidato.inventorymngmnt.data.customer

import com.fidato.inventorymngmnt.data.model.Customer

class CustomerNetworkDataProvider(private val customerService: CustomerServices) {

    suspend fun getCustomer() = customerService.getAllCustomers()

    suspend fun getCustomerById(id: Int) = customerService.getCustomerById(id)

    suspend fun saveCustomer(saveCustomerRequest: Customer) =
        customerService.saveCustomer(saveCustomerRequest)

    suspend fun updateCustomer(updateCustomerRequest: Customer) =
        customerService.updateCustomer(updateCustomerRequest)

    suspend fun deleteCustomer(deleteCustomerRequest: Customer) =
        customerService.deleteCustomer(deleteCustomerRequest)
}