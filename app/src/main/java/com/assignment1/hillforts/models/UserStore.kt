package com.assignment1.hillforts.models

interface UserStore {
    fun findAllUsers(): List<UserModel>
    fun createUser(user: UserModel)
    fun logAllUsers()
    fun updateUser(user: UserModel)
    fun deleteUser(user: UserModel)
}