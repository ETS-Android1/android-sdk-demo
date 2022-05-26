package com.geomoby.demoapp.domain.usecases.notifications

import com.geomoby.demoapp.domain.usecases.geomoby.BeaconScanChanged

data class NotificationsUseCase(
    val sendNotification: SendNotification
    )
