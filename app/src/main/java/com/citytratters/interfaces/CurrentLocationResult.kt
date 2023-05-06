package com.citytratters.interfaces

import android.location.Location

interface CurrentLocationResult {
    fun gotCurrentLocation(location: Location?)
}