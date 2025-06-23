package com.example.connectship.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.connectship.R
import com.example.connectship.model.Internship

class AppliedInternshipAdapter(private val internships: List<Internship>) :
    RecyclerView.Adapter<AppliedInternshipAdapter.ViewHolder>() {

    private val cardColors = listOf(
        "#FFCDD2", "#C8E6C9", "#BBDEFB", "#FFF9C4", "#D1C4E9", "#FFE0B2"
    )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.internshipCard)
        val titleText: TextView = view.findViewById(R.id.internshipTitle)
        val companyText: TextView = view.findViewById(R.id.internshipCompany)
        val locationText: TextView = view.findViewById(R.id.internshipLocation)
        val stipendText: TextView = view.findViewById(R.id.internshipStipend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_internship, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val internship = internships[position]

        holder.titleText.text = internship.title
        holder.companyText.text = internship.company
        holder.locationText.text = internship.location
        holder.stipendText.text = "₹ ${internship.stipend}"

        val color = cardColors.random()
        holder.cardView.setCardBackgroundColor(Color.parseColor(color))
    }

    override fun getItemCount(): Int = internships.size
}
