package com.example.collaborationstation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collaborationstation.databinding.CreateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Create : AppCompatActivity() {
    private lateinit var create: Button
    private lateinit var email: EditText
    private lateinit var pw: EditText
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: CreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩
        binding = CreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기화
        create = binding.createBtn
        email = binding.email
        pw = binding.password

        // Firebase 인증 객체 초기화
        auth = Firebase.auth

        create.setOnClickListener() {
            val emailStr = email.text.toString()
            val pwStr = pw.text.toString()

            init(emailStr, pwStr)
        }
    }

    private fun init(email: String, pw: String){
        if(email.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "이메일 혹인 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {
            auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "이메일 형식인지 확인 또는 비밀번호 6자리 이상 입력해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
