package com.example.connectship

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.connectship.recruiter.MyPostsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecruiterHomeActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var companyEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var stipendEditText: EditText
    private lateinit var postButton: Button
    private lateinit var btnMyPosts: Button

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruiter_home)

        // UI Bindings
        titleEditText = findViewById(R.id.titleEditText)
        companyEditText = findViewById(R.id.companyEditText)
        locationEditText = findViewById(R.id.locationEditText)
        stipendEditText = findViewById(R.id.stipendEditText)
        postButton = findViewById(R.id.postButton)
        btnMyPosts = findViewById(R.id.btnMyPosts)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        postButton.setOnClickListener {
            postInternship()
        }

        btnMyPosts.setOnClickListener {
            val intent = Intent(this, MyPostsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun postInternship() {
        val title = titleEditText.text.toString().trim()
        val company = companyEditText.text.toString().trim()
        val location = locationEditText.text.toString().trim()
        val stipend = stipendEditText.text.toString().trim()

        if (title.isEmpty() || company.isEmpty() || location.isEmpty() || stipend.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val internship = hashMapOf(
            "title" to title,
            "company" to company,
            "location" to location,
            "stipend" to stipend,
            "recruiterId" to auth.currentUser?.uid
        )

        firestore.collection("internships")
            .add(internship)
            .addOnSuccessListener {
                Toast.makeText(this, "Internship posted!", Toast.LENGTH_SHORT).show()
                clearForm()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearForm() {
        titleEditText.text.clear()
        companyEditText.text.clear()
        locationEditText.text.clear()
        stipendEditText.text.clear()
    }
}
