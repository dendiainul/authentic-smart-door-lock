package com.authentic.smartdoor.storage.remote.dto

import com.google.gson.annotations.SerializedName

// Camera stream only
data class CameraStreamResponse(
    val success: Boolean,
    val data: CameraStreamDto?,
    val message: String?
)

data class CameraStreamDto(
    val door_id: Int,
    @SerializedName(value = "stream_url", alternate = ["webrtc_url", "rtsp_url"]) val stream_url: String,
    val status: String,
    val resolution: String,
    val fps: Int,
    val timestamp: String
)