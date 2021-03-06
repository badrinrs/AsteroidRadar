package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.model.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()


interface NasaAPIService {
    @GET("/neo/rest/v1/feed")
    suspend fun getFeed(@Query("start_date") startDate: String,
                        @Query("end_date") endDate: String,
                        @Query("api_key") apiKey: String): String

    @GET("/planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") apiKey: String): PictureOfDay
}

object NasaAPI {
    val retrofitService : NasaAPIService by lazy {
        retrofit.create(NasaAPIService::class.java)
    }

}


