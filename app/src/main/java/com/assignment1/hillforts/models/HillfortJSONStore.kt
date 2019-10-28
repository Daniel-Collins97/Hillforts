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

const val HILLFORTS_JSON_FILE = "hillforts.json"
val hillfortsGsonBuilder: Gson = GsonBuilder().setPrettyPrinting().create()
val hillfortsListType: Type = object : TypeToken<ArrayList<HillfortModel>>() {}.type

fun generateRandomHillfortsId(): Long {
    return Random().nextLong()
}

class HillfortJSONStore(private val context: Context) : HillfortStore, AnkoLogger {

    override fun logAll() {
        hillforts.forEach { info("$it") }
    }

    var hillforts = mutableListOf<HillfortModel>()

    init {
        if (exists(context, HILLFORTS_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HillfortModel> {
        return hillforts
    }

    override fun create(hillfort: HillfortModel) {
        hillfort.id = generateRandomHillfortsId()
        hillforts.add(hillfort)
        serialize()
    }


    override fun update(hillfort: HillfortModel) {
        val foundHillfort = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.additionalNotes = hillfort.additionalNotes
            foundHillfort.description = hillfort.description
            foundHillfort.image1 = hillfort.image1
            foundHillfort.image2 = hillfort.image2
            foundHillfort.image3 = hillfort.image3
            foundHillfort.image4 = hillfort.image4
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.title = hillfort.title
            foundHillfort.visited = hillfort.visited
            foundHillfort.zoom = hillfort.zoom
            logAll()
        }
        serialize()
    }

    override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
        serialize()
    }

    private fun serialize() {
        val jsonString = hillfortsGsonBuilder.toJson(hillforts, hillfortsListType)
        write(context, HILLFORTS_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, HILLFORTS_JSON_FILE)
        hillforts = Gson().fromJson(jsonString, hillfortsListType)
    }
}