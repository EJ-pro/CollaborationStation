package com.example.collaborationstation

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Fragment_Team_Matching_Tab1 : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val contestList = mutableListOf<Contest>()
    private lateinit var adapter: ContestAdapter // 어댑터 선언

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_team_matching_tab1, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 어댑터 초기화
        adapter = ContestAdapter(contestList)
        recyclerView.adapter = adapter // 어댑터 설정

        // 기존 데이터를 초기화
        contestList.clear()

        // Firestore에서 데이터 가져오기
        db.collection("tab1")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val contest = Contest(
                        document.getString("대회명") ?: "",
                        document.getString("설명") ?: "",
                        document.getString("신청 마감일") ?: "",
                        document.getString("일정 마감") ?: "",
                        document.getString("일정 시작") ?: "",
                        document.getString("장소") ?: "",
                        document.getString("참가 자격") ?: "",
                        document.getString("이미지") ?: "",
                        document.getString("url") ?:""
                    )
                    contestList.add(contest)
                }
                adapter.notifyDataSetChanged() // 데이터 변경 시 어댑터에 알림
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기 실패 시 처리할 내용을 여기에 작성합니다.
            }

        // 리사이클러뷰 아이템 클릭 리스너 설정
        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(context, recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // 클릭한 아이템의 위치(position)에 대한 작업을 수행합니다.
                        val selectedContest = contestList[position]

                        // ContestDetailActivity로 이동하는 Intent를 생성합니다.
                        val intent = Intent(activity, ContestDetailActivity::class.java)
                        // 선택한 대회의 이름을 전달합니다.
                        intent.putExtra("contest_name", selectedContest.name)
                        intent.putExtra("contest_url", selectedContest.url)

                        // ContestDetailActivity를 시작합니다.
                        startActivity(intent)
                    }




                    override fun onLongItemClick(view: View?, position: Int) {
                        // 롱 클릭 이벤트를 처리할 경우 여기에 작성합니다.
                    }
                })
        )

        return rootView
    }
}

class RecyclerItemClickListener(
    context: android.content.Context?,
    recyclerView: RecyclerView,
    private val mListener: OnItemClickListener?
) : RecyclerView.OnItemTouchListener {
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onLongItemClick(view: View?, position: Int)
    }

    private val mGestureDetector: GestureDetector

    init {
        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null && mListener != null) {
                    mListener.onLongItemClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView = rv.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}
