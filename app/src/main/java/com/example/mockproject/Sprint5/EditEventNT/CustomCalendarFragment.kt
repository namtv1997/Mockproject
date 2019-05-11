package com.example.mockproject.Sprint5.EditEventNT


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.mockproject.R
import com.example.mockproject.Sprint5.EditEventNT.view.IpmEditEvent
import com.example.mockproject.util.swichFragment
import kotlinx.android.synthetic.main.fragment_custom_calendar.*
import java.util.*

class CustomCalendarFragment : Fragment() {
    val strBuffer = StringBuffer()
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0
    private var seconds: Int = 0
    var ipmEditEvent: IpmEditEvent? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_calendar, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get current calendar date and time.
        val currCalendar = Calendar.getInstance()

        // Set the timezone which you want to display time.
        currCalendar.timeZone = TimeZone.getTimeZone("Asia/Hong_Kong")

        year = currCalendar.get(Calendar.YEAR)
        month = currCalendar.get(Calendar.MONTH)
        day = currCalendar.get(Calendar.DAY_OF_MONTH)
        hour = currCalendar.get(Calendar.HOUR_OF_DAY)
        minute = currCalendar.get(Calendar.MINUTE)
        seconds = currCalendar.get(Calendar.SECOND)

        datePickerExample.init(
                year - 1, month + 1, day + 5
        ) { _, year, month, day ->
            this.year = year
            this.month = month
            this.day = day

            if(strBuffer.isEmpty()) {
                showUserSelectDateTime()
            }else{
                strBuffer.setLength(0)
                showUserSelectDateTime()
            }
        }
        // Get time picker object.
        timePickerExample.hour = this.hour
        timePickerExample.minute = this.minute
        timePickerExample.setIs24HourView(true)
        timePickerExample.setOnTimeChangedListener { _, hour, minute ->
            this.hour = hour
            this.minute = minute

            if(strBuffer.isEmpty()) {
                showUserSelectDateTime()
            }else{
                strBuffer.setLength(0)
                showUserSelectDateTime()
            }
        }
        btnOkCustomCalendar.setOnClickListener {
            sendCustomCalendar()
        }

    }
    private fun showUserSelectDateTime() {
        // Get TextView object which is used to show user pick date and time.

        strBuffer.append(this.year)
        strBuffer.append("/")
        if((this.month+1)<10) {
            strBuffer.append("0${this.month + 1}")
        }else{
            strBuffer.append(this.month + 1)
        }
        strBuffer.append("/")
        if(this.day < 10){
            strBuffer.append("0${this.day}")
        }else {
            strBuffer.append(this.day)
        }
        strBuffer.append(" ")
        if(this.hour < 10){
            strBuffer.append("0${this.hour}")
        }else {
            strBuffer.append(this.hour)
        }
        strBuffer.append(":")
        if(this.minute < 10){
            strBuffer.append("0${this.minute}")
        }else {
            strBuffer.append(this.minute)
        }
        strBuffer.append(":")
        if(this.seconds < 10){
            strBuffer.append("0${this.seconds}")
        }else {
            strBuffer.append(this.seconds)
        }

        textViewShowDateTime.text = strBuffer.toString()
        textViewShowDateTime.setTextColor(Color.BLUE)
        textViewShowDateTime.gravity = Gravity.CENTER
        textViewShowDateTime.textSize = 20f
    }

     fun setListener(ipmEditEvent: IpmEditEvent){
        this.ipmEditEvent = ipmEditEvent
    }

    private fun sendCustomCalendar(){
        var dateTime:String = getDateTime()
        if(strBuffer.toString().isNotEmpty()) {
            dateTime = if("start" == dateTime) {
                "start"
            }else{
                "end"
            }
            ipmEditEvent?.sendDataToFragment(dateTime, strBuffer.toString())
            activity?.onBackPressed()
        }

    }

    private fun getDateTime(): String{
        val bundle = this.arguments
        if (bundle != null) {
            return bundle.getString("dateTime")?:""
        }
        return ""
    }


}
