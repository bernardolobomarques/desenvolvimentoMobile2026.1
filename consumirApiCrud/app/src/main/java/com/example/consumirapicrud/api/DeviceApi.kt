package com.example.consumirapicrud.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DeviceApi {
    @GET("devices/")
    fun getDevices(): Call<List<Device>>

    @GET("devices/{id}")
    fun getDevice(@Path("id") id: Int): Call<Device>

    @POST("devices/")
    fun createDevice(@Body device: Device): Call<Device>

    @PUT("devices/{id}")
    fun updateDevice(@Path("id") id: Int, @Body device: Device): Call<Device>

    @DELETE("devices/{id}")
    fun deleteDevice(@Path("id") id: Int): Call<Unit>
}
