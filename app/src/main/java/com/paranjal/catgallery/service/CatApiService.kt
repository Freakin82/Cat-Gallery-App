package com.paranjal.catgallery.service

import com.paranjal.catgallery.features.data.CatImage
import retrofit2.http.GET

interface CatApiService {
    @GET("images/search?limit=10")
    suspend fun getCatImages(): List<CatImage>
}