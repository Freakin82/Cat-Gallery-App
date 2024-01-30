package com.paranjal.catgallery.network

import com.paranjal.catgallery.CatImage
import retrofit2.http.GET

interface CatApiService {
    @GET("images/search?limit=10")
    suspend fun getCatImages(): List<CatImage>
}