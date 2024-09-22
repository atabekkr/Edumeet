package com.imax.edumeet.data

import com.imax.edumeet.data.models.Notifications
import com.imax.edumeet.data.models.Register
import com.imax.edumeet.data.models.RegisterResponse
import com.imax.edumeet.data.models.Student
import com.imax.edumeet.models.CountOfNotification
import com.imax.edumeet.models.Group
import com.imax.edumeet.models.Login
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EdumeetApi {

    @POST("/student/register")
    suspend fun register(@Body data: Register) : Response<RegisterResponse>

    @POST("/student/login")
    suspend fun login(@Body data: Login) : Response<RegisterResponse>

    @GET("/notifications/{studentId}")
    suspend fun getNotifications(@Path("studentId") studentId: String) : Response<Notifications>

    @GET("/notification/{studentId}/length")
    suspend fun getCountOfNotification(@Path("studentId") studentId: String) : Response<CountOfNotification>

    @PUT("/notifications/{studentId}/read")
    suspend fun showNotification(@Path("studentId") studentId: String)

    @GET("/student/me")
    suspend fun getStudent() : Response<Student>

    @GET("/get-groups")
    suspend fun getGroups() : Response<List<Group>>

    @PUT("/student/profile")
    suspend fun editPhoneAndPassword(@Body data: Login) : Response<RegisterResponse>

    @PUT("/student/profile")
    suspend fun editName(@Body data: Group) : Response<RegisterResponse>

}