package com.example.connectship

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.connectship.recruiter.MyPostsFragment
import com.example.connectship.recruiter.PostInternshipFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecruiterDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruiter_dashboard)

        Toast.makeText(this, "RecruiterDashboardActivity loaded", Toast.LENGTH_SHORT).show()

        val bottomNav = findViewById<BottomNavigationView>(R.id.recruiterBottomNav)

        // Default fragment
        loadFragment(PostInternshipFragment())

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_post -> {
                    loadFragment(PostInternshipFragment())
                    true
                }
                R.id.nav_myposts -> {
                    loadFragment(MyPostsFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Yeh function class ke andar, but onCreate ke bahar hona chahiye
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.recruiterFragmentContainer, fragment)
            .commit()
    }
}
