package com.assignment1.hillforts.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.assignment1.hillforts.models.HillfortModel

@Database(entities = [HillfortModel::class], version = 1,  exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun hillfortDao(): HillfortDao

}