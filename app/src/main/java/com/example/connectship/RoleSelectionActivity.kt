package com.example.connectship

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RoleSelectionActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_selection)

        val internButton = findViewById<Button>(R.id.internButton)
        val recruiterButton = findViewById<Button>(R.id.recruiterButton)

        internButton.setOnClickListener {
            saveUserRoleAndProceed("intern")
        }

        recruiterButton.setOnClickListener {
            saveUserRoleAndProceed("recruiter")
        }
    }

    private fun saveUserRoleAndProceed(role: String) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId)
                .set(mapOf("role" to role))
                .addOnSuccessListener {
                    Toast.makeText(this, "Role saved: $role", Toast.LENGTH_SHORT).show()

                    if (role == "intern") {
                        startActivity(Intent(this, InternDashboardActivity::class.java))
                    } else {
                        startActivity(Intent(this, RecruiterDashboardActivity::class.java))
                    }
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save role", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
