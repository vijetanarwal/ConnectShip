package com.example.connectship.intern

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.connectship.R
import com.example.connectship.adapter.InternshipAdapter
import com.example.connectship.model.Internship
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldPath


class MyApplicationsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InternshipAdapter
    private val appliedList = mutableListOf<Internship>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_applications)

        recyclerView = findViewById(R.id.myApplicationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InternshipAdapter(appliedList)
        recyclerView.adapter = adapter

        fetchAppliedInternships()
    }

    private fun fetchAppliedInternships() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("applications")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { applications ->
                val internshipIds = applications.map { it.getString("internshipId") }

                if (internshipIds.isEmpty()) {
                    Toast.makeText(this, "No applications found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                db.collection("internships")
                    .whereIn(FieldPath.documentId(), internshipIds)
                    .get()
                    .addOnSuccessListener { internships ->
                        appliedList.clear()
                        for (doc in internships) {
                            val internship = doc.toObject(Internship::class.java)
                            appliedList.add(internship)
                        }
                        adapter.notifyDataSetChanged()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading applications", Toast.LENGTH_SHORT).show()
            }
    }
}
