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
import com.google.firebase.firestore.FirebaseFirestore

class InternHomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InternshipAdapter
    private val internshipList = mutableListOf<Internship>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intern_home, container, false)

        recyclerView = view.findViewById(R.id.internshipRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = InternshipAdapter(internshipList,showApplyButton = true)
        recyclerView.adapter = adapter

        fetchInternships()

        return view
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
                Toast.makeText(requireContext(), "Failed to load internships", Toast.LENGTH_SHORT).show()
            }
    }
}
