package com.fidato.inventorymngmnt.networking

import com.fidato.inventorymngmnt.data.master.MasterService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.8:8080/"

    var accessToken =
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXVyYWJocGFuZHlhN0BnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoicG9zdDp3cml0ZSJ9LHsiYXV0aG9yaXR5IjoidXNlcjp3cml0ZSJ9LHsiYXV0aG9yaXR5IjoidXNlcjpyZWFkIn0seyJhdXRob3JpdHkiOiJwb3N0OnJlYWQifSx7ImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImlhdCI6MTU5NDE5MjE2MiwiZXhwIjoxNTk1MzU2MjAwfQ.k3W6n9wWUioQmCWSkTEhqWTzx0YkVtfxTfV2cvfeZvrR6KTiAith-mmH5pcia_B4fwZK0w9LHYslPaxm2lXihQ"

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