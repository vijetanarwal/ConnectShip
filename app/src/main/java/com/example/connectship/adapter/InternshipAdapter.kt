package com.example.connectship.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.connectship.R
import com.example.connectship.model.Internship
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InternshipAdapter(
    private val internships: List<Internship>,
    private val showApplyButton: Boolean = true // 🔥 Toggle flag
) : RecyclerView.Adapter<InternshipAdapter.ViewHolder>() {

    private val cardColors = listOf(
        "#FFCDD2", "#C8E6C9", "#BBDEFB", "#FFF9C4", "#D1C4E9", "#FFE0B2"
    )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.internshipCard)
        val titleText: TextView = view.findViewById(R.id.internshipTitle)
        val companyText: TextView = view.findViewById(R.id.internshipCompany)
        val locationText: TextView = view.findViewById(R.id.internshipLocation)
        val stipendText: TextView = view.findViewById(R.id.internshipStipend)
        val applyButton: Button = view.findViewById(R.id.applyButton)
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

        // 🎨 Set random color
        val randomColor = cardColors.random()
        holder.cardView.setCardBackgroundColor(Color.parseColor(randomColor))

        // 👀 Hide or Show Apply Button
        holder.applyButton.visibility = if (showApplyButton) View.VISIBLE else View.GONE

        if (showApplyButton) {
            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()

            holder.applyButton.setOnClickListener {
                val userId = auth.currentUser?.uid
                val internshipId = internship.id

                if (userId != null && internshipId != null) {
                    val application = hashMapOf(
                        "userId" to userId,
                        "internshipId" to internshipId
                    )

                    db.collection("applications")
                        .add(application)
                        .addOnSuccessListener {
                            Toast.makeText(holder.itemView.context, "Applied!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(holder.itemView.context, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(holder.itemView.context, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = internships.size
}
