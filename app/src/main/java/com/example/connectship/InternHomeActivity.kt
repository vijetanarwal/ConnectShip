package com.example.connectship

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.connectship.adapter.InternshipAdapter
import com.example.connectship.model.Internship
import com.google.firebase.firestore.FirebaseFirestore

class InternHomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val internshipList = mutableListOf<Internship>()
    private lateinit var adapter: InternshipAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intern_home)

        recyclerView = findViewById(R.id.internshipRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InternshipAdapter(internshipList)
        recyclerView.adapter = adapter

        fetchInternships()
    }

    private fun fetchInternships() {
        db.collection("internships")
            .get()
            .addOnSuccessListener { documents ->
                internshipList.clear()
                for (doc in documents) {
                    val internship = doc.toObject(Internship::class.java)
                    internshipList.add(internship)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load internships", Toast.LENGTH_SHORT).show()
            }
    }
}
