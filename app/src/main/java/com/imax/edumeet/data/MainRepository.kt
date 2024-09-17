package com.imax.edumeet.data

import com.imax.edumeet.data.models.Notifications
import com.imax.edumeet.data.models.Register
import com.imax.edumeet.data.models.RegisterResponse
import com.imax.edumeet.data.models.Student
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: EdumeetApi) {

    suspend fun register(data: Register): Result<RegisterResponse> {
        return try {
            val response = api.register(data)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                Result.success(responseBody)
            } else {
                val message = response.errorBody()?.string()
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getNotifications(studentId: String): Result<Notifications> {
        return try {
            val response = api.getNotifications(studentId)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                Result.success(responseBody)
            } else {
                val message = response.errorBody()?.string()
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getStudent(): Result<Student> {
        return try {
            val response = api.getStudent()
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                Result.success(responseBody)
            } else {
                val message = response.errorBody()?.string()
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}