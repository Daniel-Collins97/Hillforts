package com.assignment1.hillforts.room

import androidx.room.*
import com.assignment1.hillforts.models.HillfortModel

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createHillfort(hillfort: HillfortModel)

    @Query("SELECT * FROM HillfortModel")
    fun findAllHillforts(): List<HillfortModel>

    @Query("select * from HillfortModel where id = :id")
    fun findHillfortById(id: Long): List<HillfortModel>

    @Update
    fun updateHillfort(hillfort: HillfortModel)

    @Delete
    fun deleteHillfort(hillfort: HillfortModel)
}