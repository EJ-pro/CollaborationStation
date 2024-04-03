package com.example.collaborationstation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Fragment_Message : Fragment() {

    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var messagesRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)

        etMessage = view.findViewById(R.id.etMessage)
        btnSend = view.findViewById(R.id.btnSend)
        auth = FirebaseAuth.getInstance()
        messagesRef = FirebaseDatabase.getInstance().getReference("messages")

        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                etMessage.setText("") // 메시지 보내고 입력창 초기화
            } else {
                Toast.makeText(requireContext(), "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun sendMessage(message: String) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId.toString())

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nickname = snapshot.child("nickname").value.toString()
                val chatMessage = ChatMessageEntity(nickname, message)
                messagesRef.push().setValue(chatMessage)
                    .addOnSuccessListener {
                        // 성공적으로 메시지를 전송한 경우
                    }
                    .addOnFailureListener { e ->
                        // 메시지 전송 실패 시
                        Toast.makeText(requireContext(), "메시지 전송에 실패했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터베이스 조회 취소 시 처리
                Toast.makeText(requireContext(), "데이터베이스 조회를 취소했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}