package com.fidato.inventorymngmnt.networking

import com.fidato.inventorymngmnt.data.customer.CustomerServices
import com.fidato.inventorymngmnt.data.master.MasterService
import com.fidato.inventorymngmnt.data.supplier.SupplierServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.8:8080/"

    var accessToken =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXVyYWJocGFuZHlhN0BnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoicG9zdDp3cml0ZSJ9LHsiYXV0aG9yaXR5IjoidXNlcjpyZWFkIn0seyJhdXRob3JpdHkiOiJ1c2VyOndyaXRlIn0seyJhdXRob3JpdHkiOiJwb3N0OnJlYWQifSx7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImlhdCI6MTU5NTU3NTY0MywiZXhwIjoxNTk2NzM4NjAwfQ.KLwyVArjpczhxEDa-toEDpvQAViUnk94GkCO8q0OanJt0IcGH0xo-Upeow3Fo5-zNUz0gFg23bDfXqzvtf8PzA"

    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val client =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(OAuthInterceptor("Bearer", accessToken))
            .build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    val MASTER_SERVICE: MasterService = getRetrofit().create(
        MasterService::class.java
    )

    val CUSTOMER_SERVICE: CustomerServices = getRetrofit().create(
        CustomerServices::class.java
    )

    val SUPPPLIER_SERVICE: SupplierServices = getRetrofit().create(
        SupplierServices::class.java
    )

    class OAuthInterceptor(private val tokenType: String, private val accessToken: String) :
        Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            var request = chain.request()
            request = request.newBuilder()
                .header("Authorization", "$tokenType $accessToken")
                .build()

            return chain.proceed(request)
        }
    }

}