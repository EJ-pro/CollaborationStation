package com.example.collaborationstation

sealed class ChatMainState {

    object UnInitialized: ChatMainState()

    object Loading: ChatMainState()

    data class Success(
        val chatList: List<ChatMessageEntity>
    ): ChatMainState()

    object Error: ChatMainState()
}