package com.fidato.inventorymngmnt.data.customer

import com.fidato.inventorymngmnt.data.model.Customer

class CustomerRepository(private val customerDataProvider: CustomerNetworkDataProvider) {

    suspend fun getCustomer() = customerDataProvider.getCustomer()

    suspend fun getCustomerById(id: Int) =
        customerDataProvider.getCustomerById(id)

    suspend fun saveCustomer(saveCustomerRequest: Customer) =
        customerDataProvider.saveCustomer(saveCustomerRequest)

    suspend fun updateCustomer(updateCustomerRequest: Customer) =
        customerDataProvider.updateCustomer(updateCustomerRequest)

    suspend fun deleteCustomer(deleteCustomerRequest: Customer) =
        customerDataProvider.deleteCustomer(deleteCustomerRequest)
}