package com.example.mockproject.Sprint5.createEventScreen.view


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil

import com.example.mockproject.R
import com.example.mockproject.Sprint5.EditEventNT.CustomCalendarFragment
import com.example.mockproject.Sprint5.EditEventNT.view.IpmEditEvent
import com.example.mockproject.Sprint5.createEventScreen.presenter.IpmCallBackLogicCreateEvent
import com.example.mockproject.Sprint5.createEventScreen.presenter.LogicCreateEvent
import com.example.mockproject.util.addFragment
import com.example.mockproject.util.showAlertDialog
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_create_event.*
import kotlinx.android.synthetic.main.fragment_edit_event_nt.*
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class CreateEventFragment : Fragment(), IpmCallBackViewCreateEvent, IpmEditEvent {
    private var idContent : String? =""
    private var ipmCallBackLogicCreateEvent :  LogicCreateEvent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ipmCallBackLogicCreateEvent = LogicCreateEvent()
    }

    override fun showDialogErrorTitleIsEmpty() {
        context?.showAlertDialog(getString(R.string.TitleIsEmpty))
    }

    override fun showDialogErrorTitleOver64Character() {
        context?.showAlertDialog(getString(R.string.TitleOver64Character))
    }

    override fun showDialogErrorDescriptionOver255Character() {
        context?.showAlertDialog(getString(R.string.DescriptionOver255Character))
    }

    override fun showDialogErrorLinkNotUrl() {
        context?.showAlertDialog(getString(R.string.LinkDocumentIsNotURL))
    }

    override fun showDialogErrorSpeakerOver32Character() {
        context?.showAlertDialog(getString(R.string.SpeakerOver32Character))
    }

    override fun showDialogErrorSpeakerIsEmpty() {
        context?.showAlertDialog(getString(R.string.SpeakerIsEmpty))
    }

    override fun showDialogErrorStartTimeIsEmpty() {
        context?.showAlertDialog(getString(R.string.StartTimeIsEmpty))
    }

    override fun showDialogErrorDurationIsEmpty() {
        context?.showAlertDialog(getString(R.string.DurationIsEmpty))
    }

    override fun showDialogCreateEventSuccess() {
        context?.showAlertDialog(getString(R.string.CreateEventSuccess))
        cancelCreateEvent()
    }

    override fun cancelCreateEvent() {
        activity?.onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_create_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle= this.arguments
        idContent = bundle?.getString(getString(R.string.idContentToCreateFragment))
        ipmCallBackLogicCreateEvent?.setView(this)

        tvStartTimeCreateEvent.setOnClickListener { startTime() }
        tvEndTimeCreateEvent.setOnClickListener {
            if(tvStartTimeCreateEvent.text.toString().isEmpty()) {
                context?.showAlertDialog(getString(R.string.choose_start_time))
            }else{
                endTime()
            }
        }

        btnAddCreateEvent.setOnClickListener {
            ipmCallBackLogicCreateEvent?.btnAddOnClick(
                    edtTittleEvent.text.toString(),
                    edtDescriptionEvent.text.toString(),
                    edtDocumentLinkEvent.text.toString(),
                    edtSpeaker.text.toString(),
                    tvStartTimeCreateEvent.text.toString(),
                    tvEndTimeCreateEvent.text.toString(),
                    idContent)
        }
        btnCancelCreateEvent.setOnClickListener {
            ipmCallBackLogicCreateEvent?.btnCancelOnClick()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        ipmCallBackLogicCreateEvent?.destroyView()
    }

    private fun startTime(){
        val bundle=Bundle()
        bundle.putString("dateTime", "start")
        val customCalendarFragment = CustomCalendarFragment()
        customCalendarFragment.arguments = bundle
        customCalendarFragment.setListener(this)
        addFragment(customCalendarFragment)
    }

    private fun endTime(){
        val bundle=Bundle()
        bundle.putString("dateTime", "end")
        val customCalendarFragment = CustomCalendarFragment()
        customCalendarFragment.arguments = bundle
        customCalendarFragment.setListener(this)
        addFragment(customCalendarFragment)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun sendDataToFragment(dateTime: String, dateAndTime: String) {
        if (dateTime == "start") {
            if(compareDateNow(dateAndTime) == true) {
                if (tvEndTimeCreateEvent.text.toString().isEmpty()) {
                    tvStartTimeCreateEvent.text = dateAndTime
                } else {
                    compareDates1(tvEndTimeCreateEvent.text.toString(), dateAndTime)
                }
            }
        }
        if (dateTime == "end"){
            compareDates(tvStartTimeCreateEvent.text.toString(), dateAndTime)
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun compareDates(date1: String, date2: String) {
        try {
            val pattern = "yyyy/MM/dd HH:mm:ss"
            val formatter = SimpleDateFormat(pattern)
            val Date1: Date = formatter.parse(date1)
            val Date2: Date = formatter.parse(date2)
            if (Date1 == Date2 || Date1.before(Date2)  ) {
                tvEndTimeCreateEvent.text = date2
            } else {
                context?.showAlertDialog(getString(R.string.choose_end_time))
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun compareDates1(date1: String, date2: String) {
        try {
            val pattern = "yyyy/MM/dd HH:mm:ss"
            val formatter = SimpleDateFormat(pattern)
            val Date1: Date = formatter.parse(date1)
            val Date2: Date = formatter.parse(date2)
            if (Date1 == Date2 || Date1.after(Date2)  ) {
                tvStartTimeCreateEvent.text = date2
            } else {
                context?.showAlertDialog(getString(R.string.choose_start_time))
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareDateNow(date: String): Boolean{
        try {
            val pattern = "yyyy/MM/dd HH:mm:ss"
            val formatter = SimpleDateFormat(pattern)
            val d: Date = formatter.parse(date)
            val now = Calendar.getInstance().time
            if (d.after(now)|| d == now) {
                return true
            } else {
                context?.showAlertDialog(getString(R.string.tv_error_start_date_small_create_content))
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return false
    }

}
