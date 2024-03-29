package com.example.collaborationstation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.lifecycle.viewModelScope

class ChatMainViewModel : ViewModel() {

    companion object {
        const val TAG = "ChatMainViewModel"
    }

    private var _chatMainLiveData = MutableLiveData<ChatMainState>(ChatMainState.UnInitialized)
    val chatMainLiveData: LiveData<ChatMainState> = _chatMainLiveData

    /*
    * 채팅 메세지 읽어오기
    * */
    fun fetchReadData(): Job = viewModelScope.launch {
        try {
            _chatMainLiveData.postValue(ChatMainState.Loading)
            val response: MutableList<ChatMessageEntity> = mutableListOf()
            val database = Firebase.database(CommonUtil.CHAT_DB_URL)
            CommonUtil.CHAT_REF = database.getReference(CommonUtil.CHAT_PATH).child(CommonUtil.CHAT_PATH_CHILD)
            CommonUtil.CHAT_REF.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for ((index, snapshot) in dataSnapshot.children.withIndex()) {
                        val chatItem = snapshot.value as HashMap<String, String>
                        val msgItem = ChatMessageEntity(chatItem["name"], chatItem["uid"], chatItem["content"], chatItem["timestamp"])
                        response.add(index, msgItem)
                    }
                    _chatMainLiveData.postValue(ChatMainState.Success(response))
                }
                override fun onCancelled(error: DatabaseError) {
                    _chatMainLiveData.postValue(ChatMainState.Error)
                }
            })
        } catch (e: Exception) {
            _chatMainLiveData.postValue(ChatMainState.Error)
        }
    }

    /*
    * 채팅 메세지 쓰기
    * */
    fun fetchWriteData(mName: String, mContent: String): Job = viewModelScope.launch {
        val database = Firebase.database(CommonUtil.CHAT_DB_URL)
        CommonUtil.CHAT_REF = database.getReference(CommonUtil.CHAT_PATH).child(CommonUtil.CHAT_PATH_CHILD)

        val uuid = UUID.randomUUID().toString()
        val chatMessageEntity = ChatMessageEntity(mName, uuid, mContent, CommonUtil.getTime(System.currentTimeMillis()))

        CommonUtil.CHAT_REF.push().setValue(chatMessageEntity)

        fetchReadData()
    }
}
