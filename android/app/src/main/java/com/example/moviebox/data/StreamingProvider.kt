package com.example.moviebox.data

data class StreamingProvider(
    val providerId: Long,
    val name: String,
    val logoPath: String
)

val sampleProviders = listOf(
    StreamingProvider(8, "Netflix", "/t2yyOv40HZeVlLjYsCsPHnWLk4W.jpg"),
    StreamingProvider(9, "Amazon Prime Video", "/ifhbNuuVnlwYy5oXAob0wVKpeAD.jpg"),
    StreamingProvider(337, "Disney+", "/uzKjVDPIyxB2BGzVKBFWVWEBWEQ.jpg")
)
