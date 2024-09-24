package com.imax.edumeet.data

import com.imax.edumeet.data.models.Notifications
import com.imax.edumeet.data.models.Rating
import com.imax.edumeet.data.models.RatingOfStudent
import com.imax.edumeet.data.models.Register
import com.imax.edumeet.data.models.RegisterResponse
import com.imax.edumeet.data.models.Student
import com.imax.edumeet.data.models.StudentScore
import com.imax.edumeet.models.Group
import com.imax.edumeet.models.Login
import com.imax.edumeet.models.SectionPercent
import com.imax.edumeet.models.SendScore
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

    suspend fun login(data: Login): Result<RegisterResponse> {
        return try {
            val response = api.login(data)
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

    suspend fun getCountOfNotification(studentId: String): Result<Int> {
        return try {
            val response = api.getCountOfNotification(studentId)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                Result.success(responseBody.length)
            } else {
                val message = response.errorBody()?.string()
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun showNotification(studentId: String) {
        try {
            api.showNotification(studentId)
        } catch (e: Exception) {
            e.printStackTrace()
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

    suspend fun getGroups(): Result<List<Group>> {
        return try {
            val response = api.getGroups()
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

    suspend fun editPhoneAndPassword(data: Login): Result<RegisterResponse> {
        return try {
            val response = api.editPhoneAndPassword(data)
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

    suspend fun editName(data: Group): Result<RegisterResponse> {
        return try {
            val response = api.editName(data)
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

    suspend fun sendPercent(data: SendScore): Result<RatingOfStudent> {
        return try {
            val response = api.sendScore(data)
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

    suspend fun getStudentScores(studentId: String): Result<StudentScore> {
        return try {
            val response = api.getStudentScores(studentId)
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

    suspend fun getRatingBySection(section: String): Result<Rating> {
        return try {
            val response = api.getRatingBySection(section)
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