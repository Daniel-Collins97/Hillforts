package com.assignment1.hillforts.room

import android.content.Context
import androidx.room.Room
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.HillfortStore

class HillfortStoreRoom(val context: Context) : HillfortStore {

    private var hillfortDao: HillfortDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "hillfort.db")
            .fallbackToDestructiveMigration()
            .build()
        hillfortDao = database.hillfortDao()
    }

    override fun logAll() {}

    override fun findAllHillforts(): List<HillfortModel> {
        return hillfortDao.findAllHillforts()
    }

    override fun findHillfortById(id: Long): List<HillfortModel> {
        return hillfortDao.findHillfortById(id)
    }

    override fun createHillfort(hillfort: HillfortModel) {
        hillfortDao.createHillfort(hillfort)
    }

    override fun updateHillfort(hillfort: HillfortModel) {
        hillfortDao.updateHillfort(hillfort)
    }

    override fun deleteHillfort(hillfort: HillfortModel) {
        hillfortDao.deleteHillfort(hillfort)
    }

    override fun clear() {}
}