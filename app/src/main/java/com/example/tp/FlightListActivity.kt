package com.example.tp

import FlightListActivityViewModel
import FlightListAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FlightListActivity : AppCompatActivity() {
    private lateinit var viewModel: FlightListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_list)

        this.viewModel = ViewModelProvider(this).get(FlightListActivityViewModel::class.java)
        Log.i("debug", intent.getLongExtra("BEGIN", 0).toString())
        Log.i("debug", intent.getLongExtra("END", 0).toString())
        this.viewModel.begin = intent.getLongExtra("BEGIN", 0)
        this.viewModel.end = intent.getLongExtra("END", 0)
        this.viewModel.isArrival = intent.getBooleanExtra("IS_ARRIVAL", false)
        this.viewModel.icao = intent.getStringExtra("ICAO").toString()

        this.viewModel.doRequest()

       viewModel.getFlightListLiveData().observe(this, Observer {
            //findViewById<TextView>(R.id.textView).text = it.toString()

            //Récupérer le recyclerView
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            // Attacher un Adapter
            recyclerView.adapter = FlightListAdapter(it)
            // Attacher un LayoutManager
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        })
    }
}