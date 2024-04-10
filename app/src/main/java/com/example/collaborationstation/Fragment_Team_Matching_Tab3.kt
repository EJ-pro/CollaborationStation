package com.example.collaborationstation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.collaborationstation.ChatListAdapter.Companion.TAG

class Fragment_Team_Matching_Tab3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_team_matching_tab3, container, false)
        val newPostButton = rootView.findViewById<Button>(R.id.newPostButton)

        newPostButton.setOnClickListener {
            // 클릭 시 다른 Fragment로 이동하는 Intent 설정
            val intent = Intent(activity, Fragment_Team_Matching_Tab3_Post::class.java)
            Log.e(TAG, "오류 체크.");
            startActivity(intent)
        }
        return rootView
    }
}
