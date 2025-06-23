package com.example.connectship

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.connectship.intern.InternHomeFragment
import com.example.connectship.intern.MyApplicationsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class InternDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intern_dashboard)

        val bottomNav = findViewById<BottomNavigationView>(R.id.internBottomNav)

        // Default: Intern Home
        loadFragment(InternHomeFragment())

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> loadFragment(InternHomeFragment())
                R.id.nav_applications -> loadFragment(MyApplicationsFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.internFragmentContainer, fragment)
            .commit()
    }
}
