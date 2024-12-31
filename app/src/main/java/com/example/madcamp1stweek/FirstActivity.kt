package com.example.madcamp1stweek

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FirstActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        textView = findViewById(R.id.textView)
        textView.text = "환영합니다! 첫 로그인을 축하드립니다."
    }
}
