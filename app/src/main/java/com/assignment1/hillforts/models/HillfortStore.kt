package com.assignment1.hillforts.models

interface HillfortStore {
    fun findAll(): List<HillfortModel>
    fun create(hillfort: HillfortModel)
    fun logAll()
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
}