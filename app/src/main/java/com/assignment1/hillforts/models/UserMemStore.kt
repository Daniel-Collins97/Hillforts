package com.assignment1.hillforts.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastUserId = 0L

internal fun getUserId(): Long {
    return lastUserId++
}

class UserMemStore : UserStore, AnkoLogger {


    val users = ArrayList<UserModel>()

    override fun findAllUsers(): List<UserModel> {
        return users
    }

    override fun createUser(user: UserModel) {
        user.id = getUserId()
        users.add(user)
        logAllUsers()
    }

    override fun updateUser(user: UserModel) {
        val foundUser = users.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.firstName = user.firstName
            foundUser.lastName = user.lastName
            foundUser.email = user.email
            foundUser.password = user.password
            logAllUsers()
        }
    }

    override fun deleteUser(user: UserModel) {
        users.remove(user)
    }

    override fun logAllUsers() {
        users.forEach { info("$it") }
    }
}