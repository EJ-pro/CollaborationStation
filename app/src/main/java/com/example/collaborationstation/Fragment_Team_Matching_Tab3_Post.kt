package com.example.collaborationstation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collaborationstation.ChatListAdapter.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Fragment_Team_Matching_Tab3_Post : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var saveButton: Button
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_team_matching_tab3_post)

        editTextTitle = findViewById(R.id.editText1)
        editTextContent = findViewById(R.id.editText2)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val title = editTextTitle.text.toString().trim()
            val content = editTextContent.text.toString().trim()

            // 사용자의 UID를 가져옵니다.
            val uid = auth.currentUser?.uid

            // Firestore에서 사용자 문서를 가져와서 닉네임을 확인합니다.
            if (uid != null) {
                db.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val username = document.getString("nickname") ?: "기본 사용자"

                            // 데이터베이스에 저장할 데이터 구성
                            val post = hashMapOf(
                                "title" to title,
                                "content" to content,
                                "username" to username
                            )

                            // Firestore에 데이터 저장
                            db.collection("Post")
                                .add(post)
                                .addOnSuccessListener {
                                    val toast = Toast.makeText(this, "글 등록에 성공하였습니다", Toast.LENGTH_SHORT)
                                    toast.show()

                                    Handler().postDelayed({
                                        toast.cancel()
                                    }, 1000)
                                }
                                .addOnFailureListener {
                                    val toast = Toast.makeText(this, "글 등록에 실패하였습니다", Toast.LENGTH_SHORT)
                                    toast.show()

                                    Handler().postDelayed({
                                        toast.cancel()
                                    }, 1000)
                                }
                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                    }
            }
        }
    }
}
