package com.the.club.common.ktx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.the.club.common.model.Geo

fun Geo?.toLatLng(): LatLng? {
    return if (this == null) null
    else {
        if (this.latitude != null && this.longitude != null) {
            LatLng(this.latitude, this.longitude)
        } else null

    }
}

fun Int.toBitmapDescriptor(context: Context): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, this)!!
    vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun LatLng.toLocation(): Location {
    val location = Location("empty")
    location.latitude = this.latitude
    location.longitude = this.longitude
    return location
}

fun Location.toLatLng(): LatLng = LatLng(this.latitude, this.longitude)
