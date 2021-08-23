package com.example.test.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RemoteRepository {
private lateinit var service: RepositoryService
    fun getRepositoryService():RepositoryService{
        if (!::service.isInitialized) {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.giphy.com")
                    .client(getClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
            service = retrofit.create(RepositoryService::class.java)
        }
        return service
    }
    private fun getClient(): OkHttpClient {

        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .callTimeout(10, TimeUnit.SECONDS)
        return client.build()
    }

}