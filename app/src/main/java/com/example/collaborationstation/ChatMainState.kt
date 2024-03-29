package com.example.collaborationstation

import com.example.collaborationstation.ChatMessageEntity

sealed class ChatMainState {

    object UnInitialized: ChatMainState()

    object Loading: ChatMainState()

    data class Success(
        val chatList: List<ChatMessageEntity>
    ): ChatMainState()

    object Error: ChatMainState()
}