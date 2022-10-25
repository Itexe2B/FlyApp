package com.example.tp

import MainViewModel
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
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
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val switchSelection = this.findViewById<Switch>(R.id.switchSelection)
        this.viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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

        viewModel.getBeginDateLiveData().observe(this) {
            beginDateLabel.text = Utils.dateFormatterCustom(it.time)
        }

        viewModel.getEndDateLiveData().observe(this) {
            endDateLabel.text = Utils.dateFormatterCustom(it.time)
        }
    }

    //https://gist.github.com/codinginflow/11e5acb69a91db8f2be0f8e495505d12
    fun openAirportChoiceDialog() {
        val airportChoice = DialogAirportChoice()
        airportChoice.show(supportFragmentManager, "example dialog")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun applyAirportTexts(airportData: AirportData) {
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