package com.assignment1.hillforts.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.assignment1.hillforts.R
import com.assignment1.hillforts.helpers.readImageFromPath
import com.assignment1.hillforts.models.HillfortModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.android.synthetic.main.card_hillfort.view.hillfortTitle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.text.NumberFormat

interface HillfortListener {
    fun onHillfortClick(hillfort: HillfortModel)
}

class HillfortAdapter constructor(
    private var hillforts: List<HillfortModel>,
    private var listener: HillfortListener
) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_hillfort,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView), AnkoLogger {

        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            itemView.hillfortTitle.text = hillfort.title
            itemView.hillfortVisited.isChecked = hillfort.visited
            itemView.hillfortLngLocation.text = hillfort.lng.toString()
            itemView.hillfortLatLocation.text = hillfort.lat.toString()
            Glide.with(itemView.context).load(hillfort.image1).into(itemView.imageIcon)
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
            itemView.ratingValue.text = NumberFormat.getInstance().format(hillfort.rating)
            itemView.favBox.isChecked = hillfort.favValue
            itemView.favBox.setOnCheckedChangeListener { buttonView, isChecked ->

                hillfort.favValue = isChecked
            }
        }
    }
}