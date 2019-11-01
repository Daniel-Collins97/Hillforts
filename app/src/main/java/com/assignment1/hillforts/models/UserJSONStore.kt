package com.assignment1.hillforts.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import com.assignment1.hillforts.helpers.*
import org.jetbrains.anko.info
import java.lang.reflect.Type
import java.util.*

const val USER_JSON_FILE = "users.json"
val usersGsonBuilder: Gson = GsonBuilder().setPrettyPrinting().create()
val usersListType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomUserId(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context) : UserStore, AnkoLogger {

    override fun logAllUsers() {
        users.forEach { info("$it") }
    }

    private var users = mutableListOf<UserModel>()

    init {
        if (exists(context, USER_JSON_FILE)) {
            deserializeUsers()
        }
    }

    override fun findAllUsers(): MutableList<UserModel> {
        return users
    }

    override fun createUser(user: UserModel) {
        user.id = generateRandomUserId()
        users.add(user)
        serializeUsers()
    }


    override fun updateUser(user: UserModel) {
        val foundUser = users.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.firstName = user.firstName
            foundUser.lastName = user.lastName
            foundUser.password = user.password
            foundUser.email = user.email
            logAllUsers()
        }
        serializeUsers()
    }

    override fun deleteUser(user: UserModel) {
        users.remove(user)
        serializeUsers()
    }

    private fun serializeUsers() {
        val jsonString = usersGsonBuilder.toJson(users, usersListType)
        write(context, USER_JSON_FILE, jsonString)
    }

    private fun deserializeUsers() {
        val jsonString = read(context, USER_JSON_FILE)
        users = Gson().fromJson(jsonString, usersListType)
    }
}