package com.example.tp

/**
 * Created by sergio on 07/11/2021
 * All rights reserved GoodBarber
 */
data class FlightTrackModel (val icao24: String,
                             val startTime: Int,
                             val endTime: Int,
                             val calllsign: Int,
                             val path: Array<Array<String>>
                             )