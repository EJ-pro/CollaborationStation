package com.example.collaborationstation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collaborationstation.databinding.CreateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class Create : AppCompatActivity() {
    private lateinit var binding: CreateBinding
    private lateinit var auth: FirebaseAuth

    private var isEmailChecked = false
    private var isNicknameChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emailEditText = binding.email
        val passwordEditText = binding.password
        val nicknameEditText = binding.nickname
        val createButton = binding.createBtn
        val checkDuplicateEmailButton = binding.checkDuplicateEmailBtn
        val checkDuplicateNicknameButton = binding.checkDuplicateNicknameBtn
        val checkBox = binding.checkBox
        val backButton = binding.createBack
        auth = Firebase.auth
        backButton.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        checkDuplicateEmailButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isBlank()) {
                val toast = Toast.makeText(this, "이메일을 입력하세요.", Toast.LENGTH_SHORT)

                toast.show()

                Handler().postDelayed(Runnable {
                    run(){
                        toast.cancel()
                    }
                },1000)
            } else {
                checkDuplicateEmail(email)
            }
        }

        checkDuplicateNicknameButton.setOnClickListener {
            val nickname = nicknameEditText.text.toString()

            if (nickname.isBlank()) {
                val toast = Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT)

                toast.show()

                Handler().postDelayed(Runnable {
                    run(){
                        toast.cancel()
                    }
                },1000)
            } else {
                checkDuplicateNickname(nickname)
            }
        }

        createButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val nickname = nicknameEditText.text.toString()

            // 중복 확인 버튼을 클릭하지 않았을 때 처리
            if (!isEmailChecked) {
                Toast.makeText(this, "이메일 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 중복 확인 버튼을 클릭하지 않았을 때 처리
            if (!isNicknameChecked) {
                Toast.makeText(this, "닉네임 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이메일과 닉네임이 중복 확인되었을 때 회원가입 진행
            createAccount(email, password, nickname)
        }
        createButton.setOnClickListener {

            val scrollView: ScrollView = findViewById(R.id.scrollView)

            if (emailEditText.hasFocus() || passwordEditText.hasFocus() || nicknameEditText.hasFocus()) {
                // 스크롤뷰를 맨 아래로 스크롤
                scrollView.postDelayed({
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                }, 100)
            }

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val nickname = nicknameEditText.text.toString()

            // 체크박스가 체크되었는지 확인
            if (!checkBox.isChecked) {
                Toast.makeText(this, "약관에 동의해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이메일이 확인되었는지 확인
            if (!isEmailChecked) {
                Toast.makeText(this, "이메일 확인을 해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 닉네임이 확인되었는지 확인
            if (!isNicknameChecked) {
                Toast.makeText(this, "닉네임 확인을 해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 등록 진행
            createAccount(email, password, nickname)
        }

    }

    private fun checkDuplicateEmail(email: String) {
        val database = Firebase.database
        val usersRef = database.getReference("users")

        usersRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isEmailChecked = snapshot.childrenCount == 0L

                    if (isEmailChecked) {
                        Toast.makeText(applicationContext, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "이미 사용 중인 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Error checking email duplication", error.toException())
                    Toast.makeText(applicationContext, "이메일 중복 확인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun checkDuplicateNickname(nickname: String) {
        val database = Firebase.database
        val usersRef = database.getReference("users")

        usersRef.orderByChild("nickname").equalTo(nickname)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isNicknameChecked = snapshot.childrenCount == 0L

                    if (isNicknameChecked) {
                        Toast.makeText(applicationContext, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Error checking nickname duplication", error.toException())
                    Toast.makeText(applicationContext, "닉네임 중복 확인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun createAccount(email: String, password: String, nickname: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공
                    val user = auth.currentUser
                    if (user != null) {
                        val db = Firebase.firestore
                        val userInfo = hashMapOf(
                            "email" to email,
                            "nickname" to nickname
                        )

                        db.collection("users").document(user.uid)
                            .set(userInfo)
                            .addOnSuccessListener {
                                Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error adding user info to Firestore", e)
                                Toast.makeText(this, "회원가입 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // 회원가입 실패
                    Log.e(TAG, "Error creating user", task.exception)
                    Toast.makeText(this, "회원가입 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    companion object {
        private const val TAG = "CreateActivity"
    }
}
