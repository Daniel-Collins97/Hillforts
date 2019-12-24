package com.assignment1.hillforts.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class UserModel (@PrimaryKey(autoGenerate = true)
                      var id: Long = 0,
                      var firstName: String = "",
                      var lastName: String = "",
                      var email: String = "",
                      var password: String = "") : Parcelable