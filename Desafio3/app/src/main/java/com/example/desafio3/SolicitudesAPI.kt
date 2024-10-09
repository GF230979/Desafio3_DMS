package com.example.desafio3

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SolicitudesAPI {
    @GET("recursos")
    suspend fun getRecursos(): List<Model>

    @POST("recursos")
    suspend fun createRecurso(@Body recurso: Model): Model

    @PUT("recursos/{id}")
    suspend fun updateRecurso(@Path("id") id: Long, @Body recurso: Model)

    @DELETE("recursos/{id}")
    suspend fun deleteRecurso(@Path("id") id: Long)
}
