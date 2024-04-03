package com.example.collaborationstation

data class ChatMessageEntity(
    var name: String? = null,
    var uid: String? = null,
    var content: String? = null,
    var timestamp: String? = null
) {
    // 매개변수 없는 기본 생성자를 추가
    constructor() : this("", "", "", "")
}
