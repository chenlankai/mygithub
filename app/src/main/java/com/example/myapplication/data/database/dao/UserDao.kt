package com.example.myapplication.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<User>)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAllUsers(): Flow<List<User>>   // Flow 可观察数据变化

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM user WHERE phone = :phone LIMIT 1")
    suspend fun getUserByPhone(phone: String): User?

    @Query("UPDATE user SET username = :username, phone = :phone, email = :email, address = :address, avatar = :avatar WHERE id = :id")
    suspend fun updateUser(id: Int, username: String, phone: String, email: String, address: String, avatar: String)

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUserById(id: Int)

    @Query("SELECT COUNT(*) FROM user")
    suspend fun getUserCount(): Int

    @Query("SELECT * FROM user ORDER BY id ASC LIMIT 1")
    suspend fun getFirstUser(): User?

    @Query("SELECT * FROM user ORDER BY id ASC")
    suspend fun getAllUsersOnce(): List<User>


}