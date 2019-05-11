package com.example.mockproject.retrofit2


import com.example.mockproject.common.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        var retrofit : Retrofit? = null
        const val URL_REAL = "http://192.168.1.3:9999/"
        fun getClient () : Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            retrofit = Retrofit.Builder()
                    .baseUrl(URL_REAL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit
        }
    }
}