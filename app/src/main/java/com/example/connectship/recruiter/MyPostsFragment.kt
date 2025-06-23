package com.example.connectship.recruiter

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

class MyPostsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InternshipAdapter
    private val postList = mutableListOf<Internship>()
    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_posts, container, false)

        recyclerView = view.findViewById(R.id.myPostsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = InternshipAdapter(postList,showApplyButton = false)
        recyclerView.adapter = adapter

        fetchPostedInternships()

        return view
    }

    private fun fetchPostedInternships() {
        db.collection("internships")
            .whereEqualTo("postedBy", currentUserId)
            .get()
            .addOnSuccessListener { documents ->
                postList.clear()
                for (doc in documents) {
                    val internship = doc.toObject(Internship::class.java)
                    postList.add(internship)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load posts", Toast.LENGTH_SHORT).show()
            }
    }
}
