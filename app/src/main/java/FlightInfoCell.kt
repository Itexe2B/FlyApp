import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.tp.FlightModel
import com.example.tp.R

@SuppressLint("ViewConstructor")
class FlightInfoCell : LinearLayout {

    //lateinit var depDateTextView: TextView
    lateinit var depAirportTextView: TextView
    lateinit var depHourTextView: TextView
    //lateinit var arrDateTextView: TextView
    lateinit var arrAirportTextView: TextView
    lateinit var arrHourTextView: TextView
    lateinit var flightDurationTextView: TextView
    lateinit var flightNameTextView: TextView


    constructor(context: Context?) : super(context) {
        initLayout()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initLayout()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initLayout()
    }

    private fun bindViews() {
        // make the find view by ids for your view
        depAirportTextView= findViewById(R.id.departLabel)
        depHourTextView= findViewById(R.id.heureDepartLabel)
        arrAirportTextView= findViewById(R.id.arriverLabel)
        arrHourTextView= findViewById(R.id.heureArriverLabel)
        flightDurationTextView= findViewById(R.id.flyTime)
        flightNameTextView= findViewById(R.id.flyNumber)
    }

    fun bindData(flight: FlightModel) {
        Log.d("TAG", "message")
        //fill your views
        //depDateTextView.text = flight.firstSeen.toString()
        depAirportTextView.text = flight.estDepartureAirport
        //depHourTextView.text =
        flightNameTextView.text = "Fly number : " + flight.callsign
        flightDurationTextView.text = "Fly time : " +(flight.lastSeen - flight.firstSeen).toString()
        //arrDateTextView.text = flight.lastSeen.toString()
        arrAirportTextView.text = flight.estArrivalAirport
        //depHourTextView.text =
    }

    private fun initLayout() {
        LayoutInflater.from(context).inflate(R.layout.flight_cell, this, true)
        bindViews()
    }

}