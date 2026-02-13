package com.minhagrana.repository.impl

import com.minhagrana.database.DatabaseHelper
import com.minhagrana.entities.User
import com.minhagrana.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserRepositoryImpl(
    private val databaseHelper: DatabaseHelper,
) : UserRepository {
    companion object {
        private const val DEFAULT_USER_NAME = "Usuário Local"
        private const val DEFAULT_USER_EMAIL = "local@minhagrana.app"
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
                email = DEFAULT_USER_EMAIL,
                password = "",
                balanceVisibility = true,
            )
        val userId = databaseHelper.insertUser(newUser)
        return newUser.copy(id = userId.toInt())
    }

    override suspend fun insertUser(user: User): Long = databaseHelper.insertUser(user)

    override suspend fun updateUser(user: User) = databaseHelper.updateUser(user)

    override suspend fun deleteUser(id: Int) = databaseHelper.deleteUser(id)
}
