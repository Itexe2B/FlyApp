package com.example.tp

import FlightListActivityViewModel
import FlightListAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
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

       /*viewModel.getFlightListLiveData().observe(this, Observer {
            //findViewById<TextView>(R.id.textView).text = it.toString()

            //Récupérer le recyclerView
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            // Attacher un Adapter
            recyclerView.adapter = FlightListAdapter(it, this)
            // Attacher un LayoutManager
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        })*/

        //val isTablet = findViewById<FragmentContainerView>(R.id.fragment_map_container) != null
        viewModel.getClickedFlightLiveData().observe(this, Observer {
            // Afficher le bon vol

            //Si c'est le telephone alors on remplace le fragment de list par la map
            //Sinon il y a deux containers
            if (true) {
                //remplacer le fragment
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_list_container, FlightMapFragment.newInstance("", ""))
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })
    }
}