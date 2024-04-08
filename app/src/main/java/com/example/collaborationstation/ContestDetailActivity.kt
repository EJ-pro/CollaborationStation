package com.example.collaborationstation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ContestDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contest_detail)

        // 인텐트에서 대회 이름을 가져옴
        val contestName = intent.getStringExtra("contest_name")
        val contestUrl = intent.getStringExtra("contest_url")

        // 대회 이름을 textViewContestTitle에 설정
        val textViewContestTitle = findViewById<TextView>(R.id.textViewContestTitle)
        textViewContestTitle.text = contestName

        val buttonUrl = findViewById<Button>(R.id.url_address)
        buttonUrl.setOnClickListener {
            // URL로 이동하는 Intent 생성
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(contestUrl))
            startActivity(intent)
        }
    }

}
