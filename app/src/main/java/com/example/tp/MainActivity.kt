package com.example.tp

import MainViewModel
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import java.util.*

class MainActivity : AppCompatActivity(), DialogAirportChoice.DialogAirportChoiceListener {
    @RequiresApi(Build.VERSION_CODES.M)
    var depart: Boolean = true
    private lateinit var viewModel: MainViewModel
    private lateinit var chooseAirport: AirportData

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val switchSelection = this.findViewById<Switch>(R.id.switchSelection)
        this.viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        this.chooseAirport = AirportData("", "", "")
        val aeroportSelection = this.findViewById<ConstraintLayout>(R.id.aeroportSelectionLayout)

        switchSelection.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchSelection.text = "Arriver"
                this.depart = false
            } else {
                switchSelection.text = "Départ"
                this.depart = true
            }
        }

        aeroportSelection.setOnClickListener { view: View ->
            openAirportChoiceDialog()
        }

        val beginDateLabel = findViewById<TextView>(R.id.departDateSelection)
        val endDateLabel = findViewById<TextView>(R.id.arriverDateSelection)
        beginDateLabel.setOnClickListener { showDatePickerDialog(MainViewModel.DateType.BEGIN) }
        endDateLabel.setOnClickListener { showDatePickerDialog(MainViewModel.DateType.END) }

        var begin: Long = 0
        var end: Long = 0

        viewModel.getBeginDateLiveData().observe(this) {
            beginDateLabel.text = Utils.dateFormatterCustom(it.time)
            begin = (it.timeInMillis / 1000).toLong()
        }

        viewModel.getEndDateLiveData().observe(this) {
            endDateLabel.text = Utils.dateFormatterCustom(it.time)
            end = (it.timeInMillis / 1000).toLong()
        }

        val searchButton = this.findViewById<ImageButton>(R.id.search)
        searchButton.setOnClickListener { view: View ->
            val intent = Intent(this, FlightListActivity::class.java)
            intent.putExtra("BEGIN", begin)
            intent.putExtra("END", end)
            intent.putExtra("IS_ARRIVAL", depart)
            intent.putExtra("ICAO", if(this.chooseAirport.icao != "") this.chooseAirport.icao else null)
            startActivity(intent)
        }


    }

    //https://gist.github.com/codinginflow/11e5acb69a91db8f2be0f8e495505d12
    fun openAirportChoiceDialog() {
        val airportChoice = DialogAirportChoice()
        airportChoice.show(supportFragmentManager, "example dialog")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun applyAirportTexts(airportData: AirportData) {
        this.chooseAirport = airportData
        val code = this.findViewById<TextView>(R.id.aeroportSelection)
        val localisation = this.findViewById<TextView>(R.id.emplacementSelection)
        code.text = airportData.code
        localisation.text = airportData.localisation
        Log.i("test Instance", this.depart.toString())
    }

    private fun showDatePickerDialog(dateType: MainViewModel.DateType) {
        // Date Select Listener.
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.updateCalendarLiveData(dateType, calendar)
            }

        val currentCalendar = if (dateType == MainViewModel.DateType.BEGIN) viewModel.getBeginDateLiveData().value else viewModel.getEndDateLiveData().value

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            currentCalendar!!.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH),
            currentCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}