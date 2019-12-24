package com.assignment1.hillforts.room

import android.content.Context
import androidx.room.Room
import com.assignment1.hillforts.models.UserModel
import com.assignment1.hillforts.models.UserStore

class UserStoreRoom(val context: Context) : UserStore {

    var userDao: UserDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "hillfort.db")
            .fallbackToDestructiveMigration()
            .build()
        userDao = database.userDao()
    }

    override fun logAllUsers() {}

    override fun findAllUsers(): List<UserModel> {
        return userDao.findAllUsers()
    }

    override fun findUserById(id: Long): List<UserModel> {
        return userDao.findUserById(id)
    }

    override fun createUser(user: UserModel) {
        userDao.createUser(user)
    }

    override fun updateUser(user: UserModel) {
        userDao.updateUser(user)
    }

    override fun deleteUser(user: UserModel) {
        userDao.deleteUser(user)
    }
}