package com.authentic.smartdoor.dashboard.data.mappers

import com.authentic.smartdoor.dashboard.domain.model.AccessLog
import com.authentic.smartdoor.dashboard.domain.model.CameraCapture
import com.authentic.smartdoor.dashboard.domain.model.Door
import com.authentic.smartdoor.dashboard.domain.model.User
import com.authentic.smartdoor.storage.remote.dto.AccessLogDto

fun AccessLogDto.toDomainModel(): AccessLog {
    return AccessLog(
        id = id.toString(),
        userId = user_id.toString(),
        doorId = door_id.toString(),
        action = action,
        timestamp = timestamp,
        success = success,
        method = method,
        ipAddress = ip_address,
        cameraCaptureId = camera_capture_id?.toString(),
        user = user?.let { userDto ->
            User(
                id = userDto.id.toString(),
                name = userDto.name,
                email = userDto.email,
                avatar = userDto.avatar,
                role = userDto.role,
                faceRegistered = userDto.face_registered
            )
        },
        door = door?.let { doorDto ->
            Door(
                id = doorDto.id.toString(),
                name = doorDto.name,
                location = doorDto.location,
                locked = doorDto.locked,
                batteryLevel = doorDto.battery_level,
                lastUpdate = doorDto.last_update,
                wifiStrength = doorDto.wifi_strength,
                cameraActive = doorDto.camera_active
            )
        },
        cameraCapture = camera_capture?.let { cameraDto ->
            CameraCapture(
                id = cameraDto.id.toString(),
                filename = cameraDto.filename,
                eventType = cameraDto.event_type,
                timestamp = cameraDto.timestamp
            )
        }
    )
}
