package com.jason.alp_vp.data.repository

import com.jason.alp_vp.data.service.*
import com.jason.alp_vp.ui.model.User as UiUser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserRepository(private val service: UserService) {

    suspend fun register(username: String, email: String, password: String, profileImage: String? = null): Pair<String, UiUser> {
        val req = UserRegisterRequest(
            username = username,
            email = email,
            password = password,
            profile_image = profileImage
        )
        val response = service.register(req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to register user: ${response.code()} - $msg")
        }
        val body = response.body()!!
        return Pair(body.token, mapToUi(body.user))
    }

    suspend fun login(email: String, password: String): Pair<String, UiUser> {
        android.util.Log.d("UserRepository", "Attempting login for email: $email")
        val req = UserLoginRequest(email = email, password = password)
        android.util.Log.d("UserRepository", "Login request created: email=$email")

        val response = service.login(req)
        android.util.Log.d("UserRepository", "Login response code: ${response.code()}")

        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            android.util.Log.e("UserRepository", "Login failed: code=${response.code()}, error=$msg")
            throw Exception("Login failed: ${response.code()} - $msg")
        }

        val body = response.body()
        if (body == null) {
            android.util.Log.e("UserRepository", "Login response body is null")
            throw Exception("Empty response from server")
        }

        android.util.Log.d("UserRepository", "Login successful: token=${body.token.take(10)}..., userId=${body.user.id}")
        return Pair(body.token, mapToUi(body.user))
    }

    suspend fun getAllUsers(): List<UiUser> {
        val response = service.getAllUsers()
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch users: ${response.code()} - $msg")
        }
        val body = response.body()!!
        return body.map { mapToUi(it) }
    }

    suspend fun getUserById(id: Int): UiUser {
        val response = service.getUserById(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to fetch user: ${response.code()} - $msg")
        }
        val body = response.body()!!
        return mapToUi(body)
    }

    suspend fun updateUser(id: Int, username: String? = null, email: String? = null, password: String? = null, profileImage: String? = null): UiUser {
        val req = UpdateUserRequest(
            username = username,
            email = email,
            password = password,
            profile_image = profileImage
        )
        val response = service.updateUser(id, req)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to update user: ${response.code()} - $msg")
        }
        val body = response.body()!!
        return mapToUi(body)
    }

    suspend fun deleteUser(id: Int) {
        val response = service.deleteUser(id)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            val msg = err ?: "no body"
            throw Exception("Failed to delete user: ${response.code()} - $msg")
        }
    }


    suspend fun uploadProfileImage(imageFile: File): String? {
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("profile_image", imageFile.name, requestBody)

        val response = service.uploadProfileImage(part)
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            throw Exception("Upload profile image failed: ${response.code()} - ${err ?: "no body"}")
        }
        return response.body()?.data?.profile_image
    }

    suspend fun deleteProfileImage(): String? {
        val response = service.deleteProfileImage()
        if (!response.isSuccessful) {
            val err = response.errorBody()?.string()
            throw Exception("Delete profile image failed: ${response.code()} - ${err ?: "no body"}")
        }
        return response.body()?.data?.profile_image
    }

    private fun mapToUi(item: UserResponse): UiUser {
        return UiUser(
            id = item.id.toString(), // Convert Int to String
            username = item.username,
            email = item.email,
            xp = item.xp ?: 0,
            avatar = item.profile_image // Map profile_image to avatar
        )
    }
}