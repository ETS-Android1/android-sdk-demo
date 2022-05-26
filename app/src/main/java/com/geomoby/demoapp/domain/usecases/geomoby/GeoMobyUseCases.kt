package com.geomoby.demoapp.domain.usecases.geomoby

data class GeoMobyUseCases(
    val setDelegate: SetDelegate,
    val beaconScanChanged: BeaconScanChanged,
    val start:Start,
    val fenceListChanged: FenceListChanged,
    val initLocationChanged: InitLocationChanged,
    val distanceChanged: DistanceChanged,
    val updateFence: UpdateFence,
    val updateFirebaseId: UpdateFirebaseId
)
