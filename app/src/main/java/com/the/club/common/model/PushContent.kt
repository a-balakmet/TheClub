package com.the.club.common.model

data class PushContent(
    val android_link: String,
    val initial_campaign: Int,
    val ios_link: String,
    val ref_id: RefId,
    val transaction_id: String,
    val uuid: String
)

data class RefId(
    val type: String,
    val value: String
)
