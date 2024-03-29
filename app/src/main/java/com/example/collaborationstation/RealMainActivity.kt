package com.example.collaborationstation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.collaborationstation.databinding.ActivityRealMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class RealMainActivity: AppCompatActivity() {

    companion object {
        const val TAG = "RealMainActivity"
    }

    lateinit var binding: ActivityRealMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRealMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }


    private fun initView() {
        binding.btnSend.setOnClickListener {

            binding.tvText.append("\n")
            binding.tvText.append(binding.etInput.text)

            // [START write_message]
            // Write a message to the database
            val database = Firebase.database("https://collaborationstation-leek-default-rtdb.firebaseio.com/")
            val myRef = database.getReference("message")

            myRef.setValue(binding.etInput.text.toString())  // 데이터 1개가 계속 수정되는 방식
//            myRef.push().setValue(binding.etInput.text.toString())  // 데이터가 계속 쌓이는 방식
            // [END write_message]

            Log.d(TAG, "myRef :: $myRef")
            binding.etInput.text.clear()

            // [START read_message]
            // Read from the database
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = dataSnapshot.getValue<String>()
                    Log.d(TAG, "Value is: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
            // [END read_message]

        }
    }
}