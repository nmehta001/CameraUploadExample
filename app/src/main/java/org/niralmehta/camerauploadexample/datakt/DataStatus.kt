package org.niralmehta.camerauploadexample.datakt

import com.squareup.moshi.Json

data class DataStatus(
    @field:Json(name = "data")
    @Json(name = "data")
    val data: Data,
    @field:Json(name = "success")
    @Json(name = "success")
    val success: Boolean,
    @field:Json(name = "status")
    @Json(name = "status")
    val status: Int
)

data class Data(
    val id: String,
    val title: String?,
    val description: String?,
    val dateTime: Long,
    val type: String,
    val animated: Boolean,
    val width: Int,
    val height: Int,
    val size: Int,
    val views: Int,
    val bandwidth: Int,
    val vote: String?,
    val favorite: Boolean,
    val nsfw: Boolean?,
    val section: String?,
    val accountUrl: String?,
    val accountId: Int,
    val isAd: Boolean,
    val inMostViral: Boolean,
    val hasSound: Boolean,
    val tags: List<String>,
    val adType: Int,
    val adUrl: String,
    val inGallery: Boolean,
    val deleteHash: String,
    val name: String,
    val link: String
)
