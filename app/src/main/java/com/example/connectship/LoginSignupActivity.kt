package com.example.connectship

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginSignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        Toast.makeText(this, "LoginSignupActivity started", Toast.LENGTH_SHORT).show()

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginSignupButton = findViewById<Button>(R.id.loginSignupButton)
        val toggleText = findViewById<TextView>(R.id.toggleText)
        val titleText = findViewById<TextView>(R.id.titleText)

        toggleText.setOnClickListener {
            isLoginMode = !isLoginMode
            if (isLoginMode) {
                titleText.text = "Login"
                loginSignupButton.text = "Login"
                toggleText.text = "Don't have an account? Sign up"
            } else {
                titleText.text = "Sign Up"
                loginSignupButton.text = "Sign Up"
                toggleText.text = "Already have an account? Login"
            }
        }

        loginSignupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isLoginMode) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            checkUserRole()
                        } else {
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            goToRoleSelection()
                        } else {
                            Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    private fun goToRoleSelection() {
        startActivity(Intent(this, RoleSelectionActivity::class.java))
        finish()
    }

    private fun checkUserRole() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { doc ->
                val role = doc.getString("role")?.lowercase()

                if (role == null) {
                    Toast.makeText(this, "User role not found. Please sign up again.", Toast.LENGTH_LONG).show()
                    auth.signOut()
                    startActivity(Intent(this, LoginSignupActivity::class.java))
                    finish()
                    return@addOnSuccessListener
                }

                when (role) {
                    "intern" -> {
                        Toast.makeText(this, "Welcome Intern", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, InternDashboardActivity::class.java))
                    }
                    "recruiter" -> {
                        Toast.makeText(this, "Welcome Recruiter", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, RecruiterDashboardActivity::class.java)) // ✅ FIXED here
                    }
                    else -> {
                        Toast.makeText(this, "User role invalid. Please sign up again.", Toast.LENGTH_LONG).show()
                        auth.signOut()
                        startActivity(Intent(this, LoginSignupActivity::class.java))
                        finish()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching user role", Toast.LENGTH_SHORT).show()
            }
    }
}
