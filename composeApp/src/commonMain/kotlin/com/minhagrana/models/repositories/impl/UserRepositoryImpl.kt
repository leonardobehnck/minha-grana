package com.minhagrana.models.repositories.impl

import com.minhagrana.database.DatabaseHelper
import com.minhagrana.entities.User
import com.minhagrana.models.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserRepositoryImpl(
    private val databaseHelper: DatabaseHelper,
) : UserRepository {
    companion object {
        private const val DEFAULT_USER_NAME = "Usuário Local"
    }

    override fun getAllUsersFlow(): Flow<List<User>> = databaseHelper.getAllUsersFlow()

    override suspend fun getUserByUuid(uuid: String): User? = databaseHelper.getUserByUuid(uuid)

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getOrCreateDefaultUser(): User {
        val existingUsers = databaseHelper.getAllUsersFlow().first()
        if (existingUsers.isNotEmpty()) {
            return existingUsers.first()
        }

        val newUser =
            User(
                uuid = Uuid.random().toString(),
                name = DEFAULT_USER_NAME,
            )
        databaseHelper.insertUser(newUser)
        return newUser
    }

    override suspend fun insertUser(user: User): Long = databaseHelper.insertUser(user)

    override suspend fun updateUser(user: User) = databaseHelper.updateUser(user)

    override suspend fun deleteUser(uuid: String) {
        databaseHelper.deleteAllData()
    }

    override suspend fun deleteAllData() = databaseHelper.deleteAllData()
}
