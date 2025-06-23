package com.example.connectship.intern

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.connectship.R
import com.example.connectship.adapter.InternshipAdapter
import com.example.connectship.model.Internship
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldPath

class MyApplicationsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InternshipAdapter
    private val applicationsList = mutableListOf<Internship>()
    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_applications, container, false)

        recyclerView = view.findViewById(R.id.myApplicationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = InternshipAdapter(applicationsList,showApplyButton = false)
        recyclerView.adapter = adapter

        fetchApplications()

        return view
    }

    private fun fetchApplications() {
        db.collection("applications")
            .whereEqualTo("userId", currentUserId)
            .get()
            .addOnSuccessListener { documents ->
                val internshipIds = documents.mapNotNull { it.getString("internshipId") }

                if (internshipIds.isEmpty()) {
                    Toast.makeText(requireContext(), "No applications found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                db.collection("internships")
                    .whereIn(FieldPath.documentId(), internshipIds)
                    .get()
                    .addOnSuccessListener { internships ->
                        applicationsList.clear()
                        for (doc in internships) {
                            val internship = doc.toObject(Internship::class.java)
                            applicationsList.add(internship)
                        }
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to load internships", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load applications", Toast.LENGTH_SHORT).show()
            }
    }
}
