package com.minhagrana.models.repositories

import com.minhagrana.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsersFlow(): Flow<List<User>>

    suspend fun getUserByUuid(uuid: String): User?

    suspend fun getOrCreateDefaultUser(): User

    suspend fun insertUser(user: User): Long

    suspend fun updateUser(user: User)

    suspend fun deleteUser(uuid: String)

    suspend fun deleteAllData()
}
