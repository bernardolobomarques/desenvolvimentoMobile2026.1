package com.metaquest.network

import com.metaquest.models.Goal
import retrofit2.Response
import retrofit2.http.*

interface GoalsApiService {
    @POST("metas")
    suspend fun createGoal(@Body goal: Goal): Response<Goal>

    @GET("metas")
    suspend fun listGoals(): Response<List<Goal>>

    @GET("metas/{id}")
    suspend fun getGoal(@Path("id") id: Long): Response<Goal>

    @PUT("metas/{id}")
    suspend fun updateGoal(@Path("id") id: Long, @Body goal: Goal): Response<Goal>

    @PATCH("metas/{id}/progresso")
    suspend fun updateProgress(@Path("id") id: Long, @Body body: Map<String, Int>): Response<Goal>

    @PATCH("metas/{id}/concluir")
    suspend fun concludeGoal(@Path("id") id: Long): Response<Goal>

    @DELETE("metas/{id}")
    suspend fun deleteGoal(@Path("id") id: Long): Response<Unit>
}
