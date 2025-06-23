package com.example.connectship.recruiter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.connectship.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class PostInternshipFragment : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var companyEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var stipendEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var postButton: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_internship, container, false)

        titleEditText = view.findViewById(R.id.titleEditText)
        companyEditText = view.findViewById(R.id.companyEditText)
        locationEditText = view.findViewById(R.id.locationEditText)
        stipendEditText = view.findViewById(R.id.stipendEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        postButton = view.findViewById(R.id.postButton)

        postButton.setOnClickListener {
            postInternship()
        }

        return view
    }

    private fun postInternship() {
        val title = titleEditText.text.toString().trim()
        val company = companyEditText.text.toString().trim()
        val location = locationEditText.text.toString().trim()
        val stipend = stipendEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        if (title.isEmpty() || company.isEmpty() || location.isEmpty() || stipend.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val internshipId = UUID.randomUUID().toString()

        val internship = hashMapOf(
            "id" to internshipId,
            "title" to title,
            "company" to company,
            "location" to location,
            "stipend" to stipend,
            "description" to description,
            "postedBy" to FirebaseAuth.getInstance().currentUser?.uid
        )


        db.collection("internships")
            .document(internshipId)
            .set(internship)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Internship posted!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to post internship", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        titleEditText.text.clear()
        companyEditText.text.clear()
        locationEditText.text.clear()
        stipendEditText.text.clear()
        descriptionEditText.text.clear()
    }
}
