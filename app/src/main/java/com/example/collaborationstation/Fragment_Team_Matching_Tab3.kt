package com.example.collaborationstation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Fragment_Team_Matching_Tab3 : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var newPostButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_team_matching_tab3, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)
        newPostButton = rootView.findViewById(R.id.newPostButton)

        setupRecyclerView()

        newPostButton.setOnClickListener {
            // 클릭 시 다른 Fragment로 이동하는 Intent 설정
            val intent = Intent(activity, Fragment_Team_Matching_Tab3_Post::class.java)
            startActivity(intent)
        }

        loadPosts()

        return rootView
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun loadPosts() {
        db.collection("Post")
            .get()
            .addOnSuccessListener { result ->
                val postList = mutableListOf<Post>()

                for (document in result) {
                    val title = document.getString("title")
                    val content = document.getString("content")
                    val username = document.getString("username")

                    val post = Post(title ?: "", content ?: "", username ?: "")
                    postList.add(post)
                }

                adapter.setPosts(postList)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
                Toast.makeText(context, "게시물을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        const val TAG = "FragmentTeamMatchingTab3"
    }
}
