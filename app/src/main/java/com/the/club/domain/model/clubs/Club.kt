package com.the.club.domain.model.clubs

data class Club(
    val description: String,
    val documentUrl: String,
    val id: Int,
    val imageUrl: String,
    val isAdult: Boolean,
    val name: String,
    var isMember: Boolean
)
