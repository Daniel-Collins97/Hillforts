package com.assignment1.hillforts.models

interface HillfortStore {
    fun findAllHillforts(): List<HillfortModel>
    fun createHillfort(hillfort: HillfortModel)
    fun logAll()
    fun updateHillfort(hillfort: HillfortModel)
    fun deleteHillfort(hillfort: HillfortModel)
    fun findHillfortById(id: Long): List<HillfortModel>
}