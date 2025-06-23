package com.example.connectship.recruiter

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

class MyPostsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InternshipAdapter
    private val internshipList = mutableListOf<Internship>()
    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.connectship.R.layout.activity_my_posts)

        recyclerView = findViewById(R.id.myPostsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InternshipAdapter(internshipList)
        recyclerView.adapter = adapter

        fetchMyInternships()
    }

    private fun fetchMyInternships() {
        db.collection("internships")
            .whereEqualTo("recruiterId", currentUserId)
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
                Toast.makeText(this, "Failed to load your posts", Toast.LENGTH_SHORT).show()
            }
    }
}
