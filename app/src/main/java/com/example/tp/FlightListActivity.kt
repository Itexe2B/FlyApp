package com.example.tp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class FlightListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val begin = intent.getLongExtra("BEGIN", 0)
        val end = intent.getLongExtra("END", 0)
        val isArrival = intent.getBooleanExtra("IS_ARRIVAL", false)
        val icao = intent.getStringExtra("ICAO")

        Log.i("1", begin.toString())
        Log.i("2", end.toString())
        Log.i("3", isArrival.toString())
        Log.i("4", icao.toString())
    }
}