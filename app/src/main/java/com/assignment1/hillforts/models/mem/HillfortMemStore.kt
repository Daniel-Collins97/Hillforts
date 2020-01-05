package com.assignment1.hillforts.models.mem

import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.HillfortStore

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class HillfortMemStore : HillfortStore {

    val hillforts = ArrayList<HillfortModel>()

    override fun findAllHillforts(): List<HillfortModel> {
        return hillforts
    }

    override fun createHillfort(hillfort: HillfortModel) {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll()
    }

    override fun updateHillfort(hillfort: HillfortModel) {
        val foundHillfort = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.image1 = hillfort.image1
            foundHillfort.image2 = hillfort.image2
            foundHillfort.image3 = hillfort.image3
            foundHillfort.image4 = hillfort.image4
            foundHillfort.visited = hillfort.visited
            foundHillfort.additionalNotes = foundHillfort.additionalNotes
            foundHillfort.description = foundHillfort.additionalNotes
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            logAll()
        }
    }

    override fun findHillfortById(id: Long): List<HillfortModel> {
        return hillforts.filter { it.id == id }
    }

    override fun deleteHillfort(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
    }

    override fun logAll() {}

    override fun clear() {
        hillforts.clear()
    }
}