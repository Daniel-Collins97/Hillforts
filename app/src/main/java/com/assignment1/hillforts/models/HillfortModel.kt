package com.assignment1.hillforts.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class HillfortModel (@PrimaryKey(autoGenerate = true)
                          var id: Long = 0,
                          var fbId : String = "",
                          var rating: Float = 0f,
                          var favValue: Boolean = false,
                          var userId: String = "",
                          var title: String = "",
                          var description: String = "",
                          var image1: String = "",
                          var image2: String = "",
                          var image3: String = "",
                          var image4: String = "",
                          var visited: Boolean = false,
                          var additionalNotes: String = "",
                          var lat : Double = 0.0,
                          var lng: Double = 0.0,
                          var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable


