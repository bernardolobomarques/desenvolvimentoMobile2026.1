package com.metaquest.network

import com.metaquest.models.GamificationSummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface GamificationApiService {
    @GET("gamificacao/resumo")
    suspend fun getSummary(): Response<GamificationSummary>

    @POST("gamificacao/inicializar")
    suspend fun inicializar(): Response<GamificationSummary>
}
