package com.example.collaborationstation

data class Contest(
    val name: String,
    val description: String,
    val applicationDeadline: String,
    val scheduleDeadline: String,
    val scheduleStart: String,
    val location: String,
    val eligibility: String,
    val imageReference: String,
    val url: String
)