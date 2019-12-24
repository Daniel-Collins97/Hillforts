package com.assignment1.hillforts.room

import androidx.room.*
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.UserModel

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createHillfort(hillfort: HillfortModel)

    @Query("SELECT * FROM HillfortModel")
    fun findAllHillforts(): List<HillfortModel>

    @Query("select * from HillfortModel where id = :id")
    fun findHillfortById(id: Long): List<HillfortModel>

    @Update
    fun updateHillfort(hillfort: HillfortModel)

    @Delete
    fun deleteHillfort(hillfort: HillfortModel)
}

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(user: UserModel)

    @Query("SELECT * FROM UserModel")
    fun findAllUsers(): List<UserModel>

    @Query("select * from UserModel where id = :id")
    fun findUserById(id: Long): List<UserModel>

    @Update
    fun updateUser(user: UserModel)

    @Delete
    fun deleteUser(user: UserModel)
}