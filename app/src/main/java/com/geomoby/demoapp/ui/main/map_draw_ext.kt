package com.geomoby.demoapp.ui.main

import android.graphics.Color
import com.geomoby.classes.GeomobyFenceView
import com.geomoby.demoapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

private val mFillColor = Color.argb(
    (255 * 0.5f).toInt(), (255 * 0.0f).toInt(), (255 * 0.72f).toInt(), (255 * 0.85f).toInt()
)
private val mBorderColor = Color.argb(
    (255 * 1.0f).toInt(), (255 * 0.0f).toInt(), (255 * 0.72f).toInt(), (255 * 0.85f).toInt()
)
private val mBeaconFillColor = Color.argb(
    (255 * 0.1f).toInt(), (255 * 0.0f).toInt(), (255 * 0.62f).toInt(), (255 * 0.95f).toInt()
)
private val mBeaconBorderColor = Color.argb(
    (255 * 0.0f).toInt(), (255 * 1.0f).toInt(), (255 * 1.0f).toInt(), (255 * 1.0f).toInt()
)

private const val mStrokeWidth = 6.0f

internal fun addPolygon(mMap: GoogleMap, fence: GeomobyFenceView) {
    val geometries = fence.geometries
    for (geomerty in geometries!!) {
        val points = geomerty!!.points
        val polygonOptions = PolygonOptions()
        for (point in points!!) {
            polygonOptions.add(LatLng(point!!.latitude, point.longitude))
        }
        polygonOptions.fillColor(mFillColor)
        polygonOptions.strokeColor(mBorderColor)
        polygonOptions.strokeWidth(mStrokeWidth)
        mMap.addPolygon(polygonOptions)
        val circleOptionsGeo = CircleOptions()
        circleOptionsGeo.center(
            LatLng(
                geomerty.centerPointY.toDouble(),
                geomerty.centerPointX.toDouble()
            )
        )
        circleOptionsGeo.radius(geomerty.fenceRadius.toDouble())
        circleOptionsGeo.fillColor(mBeaconFillColor)
        circleOptionsGeo.strokeColor(mBeaconBorderColor)
        circleOptionsGeo.strokeWidth(mStrokeWidth)
        mMap.addCircle(circleOptionsGeo)
    }
}

internal fun addCircle(mMap: GoogleMap, fence: GeomobyFenceView) {
    val point = fence.geometries!![0]!!.points!![0]!!
    val circleOptions = CircleOptions()
    circleOptions.center(LatLng(point.latitude, point.longitude))
    circleOptions.radius(fence.radius.toDouble())
    circleOptions.fillColor(mFillColor)
    circleOptions.strokeColor(mBorderColor)
    circleOptions.strokeWidth(mStrokeWidth)
    mMap.addCircle(circleOptions)
    val circleOptionsGeo = CircleOptions()
    circleOptionsGeo.center(LatLng(point.latitude, point.longitude))
    circleOptionsGeo.radius((fence.radius + 300).toDouble())
    circleOptionsGeo.fillColor(mBeaconFillColor)
    circleOptionsGeo.strokeColor(mBeaconBorderColor)
    circleOptionsGeo.strokeWidth(mStrokeWidth)
    mMap.addCircle(circleOptionsGeo)
}

internal fun addBeacon(mMap: GoogleMap, fence: GeomobyFenceView) {
    val point = fence.geometries!![0]!!.points!![0]!!
    val beaconOptions = CircleOptions()
        .center(LatLng(point.latitude, point.longitude))
        .radius(fence.radius.toDouble())
        .fillColor(mBeaconFillColor)
        .strokeColor(mBeaconBorderColor)
        .strokeWidth(mStrokeWidth)
    mMap.addCircle(beaconOptions)
    val markerOptions = MarkerOptions()
        .position(LatLng(point.latitude, point.longitude))
        .anchor(0.5f, 0.5f)
        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.beacon))
    mMap.addMarker(markerOptions)
}

internal fun addLine(mMap: GoogleMap, fence: GeomobyFenceView) {
    val (_, _, _, _, points) = fence.geometries!![0]!!
    val lineOptions = PolylineOptions()
    for (point in points!!) {
        val circleOptionsGeo = CircleOptions()
        circleOptionsGeo.center(LatLng(point!!.latitude, point.longitude))
        circleOptionsGeo.radius(300.0)
        circleOptionsGeo.fillColor(mBeaconFillColor)
        circleOptionsGeo.strokeColor(mBeaconBorderColor)
        circleOptionsGeo.strokeWidth(mStrokeWidth)
        mMap.addCircle(circleOptionsGeo)
        lineOptions.add(LatLng(point.latitude, point.longitude))
    }
    lineOptions.color(mBorderColor)
    lineOptions.width(mStrokeWidth)
    mMap.addPolyline(lineOptions)
}