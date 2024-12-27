package com.example.madcamp1stweek

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // 프래그먼트 인스턴스 생성
    private val homeFragment = HomeFragment()
    private val dashboardFragment = DashboardFragment()
    private val notificationsFragment = NotificationsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 초기 화면 설정 (홈 프래그먼트)
        loadFragment(homeFragment)

        // BottomNavigationView 설정
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> {
                    loadFragment(homeFragment)
                    true
                }
                R.id.nav_dashboard -> {
                    loadFragment(dashboardFragment)
                    true
                }
                R.id.nav_notifications -> {
                    loadFragment(notificationsFragment)
                    true
                }
                else -> false
            }
        }
    }

    // 프래그먼트 전환 메서드
    private fun loadFragment(fragment: Fragment){
        // 프래그먼트 매니저를 사용하여 프래그먼트 교체
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

