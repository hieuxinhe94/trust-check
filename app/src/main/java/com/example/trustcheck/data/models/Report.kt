package com.example.trustcheck.data.models

data class Report(
    val name: String,
    val cheatTitle: String,
    val cheatMethod: String,
    val cheatType: List<String>
)
