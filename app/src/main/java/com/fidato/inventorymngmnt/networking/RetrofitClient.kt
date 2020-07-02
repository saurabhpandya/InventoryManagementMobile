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
        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXVyYWJocGFuZHlhN0BnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn0seyJhdXRob3JpdHkiOiJ1c2VyOnJlYWQifSx7ImF1dGhvcml0eSI6InBvc3Q6cmVhZCJ9XSwiaWF0IjoxNTkzMDExMDAwLCJleHAiOjE1OTQxNDY2MDB9.qmTkRuBGI-4A5yALwjV-1MLdvIW8ZEsetexqfmk7PeFw8ZimNRToFBjD3U8IvTdOHEgkxvIdEzANd17hos3xtw"

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