package com.the.club.domain.repository

import com.the.club.data.remote.profile.dto.DeviceInfo

interface DeviceInfoRepository {
	fun fetch(token: String): DeviceInfo
}