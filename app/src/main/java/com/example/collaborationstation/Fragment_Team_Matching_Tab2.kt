package com.example.collaborationstation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Fragment_Team_Matching_Tab2 : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val contestList = mutableListOf<Contest>()
    private lateinit var adapter: Conference_Adapter // 어댑터 선언

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_team_matching_tab2, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 어댑터 초기화
        adapter = Conference_Adapter(contestList)
        recyclerView.adapter = adapter // 어댑터 설정

        // 기존 데이터를 초기화
        contestList.clear()

        // Firestore에서 데이터 가져오기
        db.collection("tab2")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val contest = Contest(
                        document.getString("컨퍼런스명") ?: "",
                        document.getString("설명") ?: "",
                        document.getString("신청 마감일") ?: "",
                        document.getString("일정 마감") ?: "",
                        document.getString("일정 시작") ?: "",
                        document.getString("장소") ?: "",
                        document.getString("참가 자격") ?: "",
                        document.getString("이미지") ?: ""
                    )
                    contestList.add(contest)
                }
                adapter.notifyDataSetChanged() // 데이터 변경 시 어댑터에 알림
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리할 내용을 여기에 작성합니다.
            }

        return rootView
    }
}