package com.paranjal.catgallery.network

import com.paranjal.catgallery.CatImage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CatApiService {
    @GET("images/search?limit=10")
    suspend fun getCatImages(): List<CatImage>

    companion object {
        fun create(): CatApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(CatApiService::class.java)
        }
    }
}