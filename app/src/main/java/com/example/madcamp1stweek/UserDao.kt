// UserDao.kt
package com.example.madcamp1stweek

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    // 회원가입: 새로운 사용자 추가
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    // 로그인: ID와 Password가 일치하는 사용자 찾기
    @Query("SELECT * FROM users WHERE id = :id AND password = :password LIMIT 1")
    suspend fun getUser(id: String, password: String): User?

    // ID 중복 확인
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): User?
}
