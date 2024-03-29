package com.example.tp

import FlightListActivityViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FlightMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FlightMapFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel : FlightListActivityViewModel
    private var mapView: MapView? = null
    private lateinit var flightModel: FlightModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val moreInformation = view.findViewById<Button>(R.id.plusInformation)
        moreInformation.setOnClickListener {
            //change fragment
            val isPhone = view.findViewById<FragmentContainerView>(R.id.fragment_map_container) == null

            val transaction = parentFragmentManager.beginTransaction()
            if (isPhone){
                //Si c'est un téléphone le fragment map container n'existe pas, la vu actuelle
                //est le fragment_List_Container
                transaction.replace(R.id.fragment_list_container, FlightInformation.newInstance("", ""))
            } else {
                transaction.replace(R.id.fragment_map_container, FlightInformation.newInstance("", ""))
            }
            transaction.addToBackStack(null)
            transaction.commit()
        }
        view.visibility = View.INVISIBLE
        view.isEnabled = false
        mapView = view.findViewById<MapView>(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(FlightListActivityViewModel::class.java)
        viewModel.getClickedFlightLiveData().observe(viewLifecycleOwner, Observer {
            view.visibility = View.VISIBLE
            view.isEnabled = true
            flightModel = it
            view.findViewById<TextView>(R.id.callSignInformation).text = "Fly number : " + it.callsign
            view.findViewById<TextView>(R.id.departLabelInformation).text = it.estDepartureAirport
            view.findViewById<TextView>(R.id.arriverLabelInformation).text = it.estArrivalAirport
            view.findViewById<TextView>(R.id.flyTimeInformation).text = "%02d:%02d".format(Date(it.lastSeen * 1000 - it.firstSeen * 1000).hours, Date(it.lastSeen * 1000 - it.firstSeen * 1000).minutes)
            view.findViewById<TextView>(R.id.heureArriverLabelInformation).text = "%02d:%02d".format(Date(it.lastSeen * 1000).hours, Date(it.lastSeen * 1000).minutes)
            mapView!!.getMapAsync(this)
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.getPositionOfClickedFlight()

        viewModel.getFlightTrackListLiveData().observe(viewLifecycleOwner) {
            val polylineOptions = PolylineOptions()
            var firstItem = true
            var latitude: Double? = null
            var lontitude: Double? = null
            googleMap.clear()
            it.path.forEach {
                latitude = it[1].toDouble()
                lontitude = it[2].toDouble()
                polylineOptions.add(LatLng(latitude!!, lontitude!!))
                if (firstItem) {
                    firstItem = false
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(latitude!!, lontitude!!))
                            .title("Départ")
                    )
                }
            }
            if (latitude != null && lontitude != null) {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(latitude!!, lontitude!!))
                        .title("Arrivé")
                )
            }
            googleMap.addPolyline(polylineOptions)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_map, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FlightMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FlightMapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        if (mapView != null) {
            mapView!!.onResume()
        }
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapView != null) {
            mapView!!.onDestroy()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (mapView != null) {
            mapView!!.onLowMemory()
        }
    }
}