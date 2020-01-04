package com.assignment1.hillforts.firebase

import android.content.Context
import android.graphics.Bitmap
import com.assignment1.hillforts.helpers.readImageFromPath
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.HillfortStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream
import java.io.File

class HillfortFireStore(val context: Context) : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    private lateinit var userId: String
    private lateinit var db: DatabaseReference
    private var st: StorageReference = FirebaseStorage.getInstance().reference



    override fun logAll() {}

    override fun findAllHillforts(): ArrayList<HillfortModel> {
        return hillforts
    }

    override fun findHillfortById(id: Long): List<HillfortModel> {
        return hillforts.filter { it.id == id }
    }

    override fun createHillfort(hillfort: HillfortModel) {
        val key: String = db.child("users").child(userId).child("hillforts").push().key!!
        key.let {
            hillfort.fbId = key
            hillforts.add(hillfort)
            db.child("users").child(userId).child("hillforts").child(key).setValue(hillfort)
            updateImage(hillfort)
        }
    }

    override fun updateHillfort (hillfort: HillfortModel) {
        val foundHillfort = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.image1 = hillfort.image1
            foundHillfort.image2 = hillfort.image2
            foundHillfort.image3 = hillfort.image3
            foundHillfort.image4 = hillfort.image4
            foundHillfort.visited = hillfort.visited
            foundHillfort.additionalNotes = hillfort.additionalNotes
            foundHillfort.description = hillfort.description
            foundHillfort.rating = hillfort.rating
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
        }
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
        if ((hillfort.image1.length) > 0 && (hillfort.image1 != "h")) {
            updateImage(hillfort)
        }
        if ((hillfort.image2.length) > 0 && (hillfort.image2 != "h")) {
            updateImage(hillfort)
        }
        if ((hillfort.image3.length) > 0 && (hillfort.image3 != "h")) {
            updateImage(hillfort)
        }
        if ((hillfort.image4.length) > 0 && (hillfort.image4 != "h")) {
            updateImage(hillfort)
        }
    }

    override fun deleteHillfort(hillfort: HillfortModel) {
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }

    override fun clear() {
        hillforts.clear()
    }

    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(hillforts) { it.getValue(HillfortModel()::class.java) }
                hillfortsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }

    private fun updateImage(hillfort: HillfortModel) {
        when {
            hillfort.image1 != "" -> {
                val fileName = File(hillfort.image1)
                val imageName = fileName.name

                info("@@@ ST: $st")
                val imageRef = st.child("$userId/$imageName")
                info("@@@ IMAGEREF: $imageRef")
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, hillfort.image1)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println(it.message)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            hillfort.image1 = it.toString()
                            db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
                        }
                    }
                }
            }
            hillfort.image2 != "" -> {
                val fileName = File(hillfort.image2)
                val imageName = fileName.name

                val imageRef = st.child("$userId/$imageName")
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, hillfort.image2)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println(it.message)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            hillfort.image2 = it.toString()
                            db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
                        }
                    }
                }
            }
            hillfort.image3 != "" -> {
                val fileName = File(hillfort.image3)
                val imageName = fileName.name

                val imageRef = st.child("$userId/$imageName")
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, hillfort.image3)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println(it.message)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            hillfort.image3 = it.toString()
                            db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
                        }
                    }
                }
            }
            hillfort.image4 != "" -> {
                val fileName = File(hillfort.image4)
                val imageName = fileName.name

                val imageRef = st.child("$userId/$imageName")
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, hillfort.image4)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println(it.message)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            hillfort.image4 = it.toString()
                            db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
                        }
                    }
                }
            }
        }
    }
}