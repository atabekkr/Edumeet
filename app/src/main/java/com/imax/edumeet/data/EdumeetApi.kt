package com.imax.edumeet.data

import com.imax.edumeet.data.models.Notifications
import com.imax.edumeet.data.models.Register
import com.imax.edumeet.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EdumeetApi {

    @POST("/student/register")
    suspend fun register(@Body data: Register) : Response<RegisterResponse>

    @GET("/notifications/{studentId}")
    suspend fun getNotifications(@Path("studentId") studentId: String) : Response<Notifications>

}