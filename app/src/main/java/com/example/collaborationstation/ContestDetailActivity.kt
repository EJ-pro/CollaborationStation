package com.example.collaborationstation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ContestDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contest_detail)

        // 인텐트에서 대회 이름을 가져옴
        val contestName = intent.getStringExtra("contest_name")

        // 대회 이름을 textViewContestTitle에 설정
        val textViewContestTitle = findViewById<TextView>(R.id.textViewContestTitle)
        textViewContestTitle.text = contestName
    }

}
