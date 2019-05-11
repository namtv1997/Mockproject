package com.example.mockproject.Sprint5.eventDetail


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView

import com.example.mockproject.R
import com.example.mockproject.Sprint5.AttendanceEventNT.AttendanceEventNTFragment
import com.example.mockproject.Sprint5.EditEventNT.CustomCalendarFragment
import com.example.mockproject.Sprint5.EditEventNT.EditEventNTFragment
import com.example.mockproject.Sprint5.EditEventNT.view.IpmEditEvent
import com.example.mockproject.Sprint5.calendarScreen.model.EventModel
import com.example.mockproject.Sprint5.createEventScreen.model.CreateEventModelPostServer
import com.example.mockproject.common.Constant
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.BaseDataResponseServer
import com.example.mockproject.util.addFragment
import com.example.mockproject.util.showAlertDialog
import com.example.mockproject.util.swichFragment
import kotlinx.android.synthetic.main.fragment_create_event.*
import kotlinx.android.synthetic.main.fragment_edit_event_nt.*
import kotlinx.android.synthetic.main.fragment_event_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*



class
EventDetailFragment : Fragment(), IpmEditEvent {

    private  var eventDetail: EventModel? = null
    private var dataClient: DataClient? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bundle = arguments


        var event = EventModel(bundle?.getString("eventId")!!,
                bundle.getString("titte"), bundle.getBoolean("fromContent"),
                bundle.getString("link"),
                bundle.getString("speaker"),
                bundle.getString("startDete"),
                "",
                bundle.getString("endDate"),
                bundle.getString("description")
        )
        eventDetail = event
        tvStartDateDetailEvent.setOnClickListener { startTime() }
        tvEndDateDetailEvent.setOnClickListener {
            if(tvStartDateDetailEvent.text.toString().isEmpty()) {
            context?.showAlertDialog(getString(R.string.choose_start_time))
        }else{
            endTime()
        }
        }
        setupWidget(event)

        val startDate = event.startDateTime
        val endDate = event.endDateTime

        val dateCurrent = Calendar.getInstance().getTime()
        val getStartdate = convertStringToDate(startDate)
        val getEnddate = convertStringToDate(endDate)


        if (Constant.id == Constant.idCaptainWhenClickGroup) {
            btnDeleteEvent.visibility = View.VISIBLE
            btnEditEvent.visibility = View.VISIBLE
            if (
                    (getStartdate.before(dateCurrent) && getEnddate.after(dateCurrent))
                    || dateCurrent == getStartdate
                    || dateCurrent == getEnddate
            ) {
                btnAttendanceEvent.visibility = View.VISIBLE
            }
            btnDeleteEvent.setOnClickListener {
                val dialog = Dialog(context)
                dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.fragment_dialog_choose)
                dialog.show()
                val textView = dialog.findViewById<TextView>(R.id.tv_message)
                textView.text = getString(R.string.deleteEvent)
                val textViewYes = dialog.findViewById<TextView>(R.id.tv_yes)
                textViewYes.setOnClickListener {
                    dataClient = RetrofitClient.getClient()?.create(DataClient::class.java)
                    val call = dataClient?.deleteEvent(event.id, Constant.idCaptainWhenClickGroup!!)
                    call?.enqueue(object : Callback<BaseDataResponseServer<Any>> {
                        override fun onFailure(call: Call<BaseDataResponseServer<Any>>?, t: Throwable?) {

                        }

                        override fun onResponse(call: Call<BaseDataResponseServer<Any>>?, response: Response<BaseDataResponseServer<Any>>?) {
                            if (response?.body()?.code == 0) {
                                context?.showAlertDialog("Delete event sucsess")
                                dialog.cancel()
                            }
                        }
                    })
                    activity?.onBackPressed()
                }
                val textViewNo = dialog.findViewById<TextView>(R.id.tv_no)
                textViewNo.setOnClickListener {
                    dialog.cancel()

                }
            }
        }
        if (event.fromContent) {
            tvTitleEventDetail.setBackgroundColor(Color.RED)
        }
        btnAttendanceEvent.setOnClickListener {
            var bundleFromDetailToAttendance = Bundle()
            bundleFromDetailToAttendance.putString("EventIdFromDetailToAttendance", event.id)

            val attendanceEventNTFragment = AttendanceEventNTFragment()
            attendanceEventNTFragment.arguments = bundleFromDetailToAttendance
            swichFragment(attendanceEventNTFragment)
        }

        btnEditEvent.setOnClickListener {
            tvTitleEventDetail.isEnabled = true
            tvDescriptionEventDetail.isEnabled = true
            linkDetail.isEnabled = true
            tvSpeakerDetail.isEnabled = true
            tvStartDateDetailEvent.isEnabled = true
            tvEndDateDetailEvent.isEnabled = true
            btnSaveDetailEvent.visibility = View.VISIBLE
//            val editEventNTFragment = EditEventNTFragment(event)
//            editEventNTFragment.setTargetFragment(this, Constant.DIALOG_FRAGMENT)
//            editEventNTFragment.show(activity?.supportFragmentManager, "fragment_edit_name")
        }
        btnOK.setOnClickListener {
            activity?.onBackPressed()
        }
        btnSaveDetailEvent.setOnClickListener { onPostEditEvent() }
    }

    private fun setupWidget(event:EventModel){
        tvTitleEventDetail.setText(event.title)
        tvDescriptionEventDetail.setText(event.description)
        linkDetail.setText(event.documentLink)
        tvSpeakerDetail.setText(event.speakerLink)
        tvStartDateDetailEvent.text = event.startDateTime
        tvEndDateDetailEvent.text = event.endDateTime
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Constant.DIALOG_FRAGMENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bundle = data?.extras
                    val eventModel  = bundle?.getSerializable("EventDialog") as EventModel
                    setupWidget(eventModel)
                } else if (resultCode == Activity.RESULT_CANCELED){

                }
            }
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

    @SuppressLint("SimpleDateFormat")
    private fun compareDates1(date1: String, date2: String) {
        try {
            val pattern = "yyyy/MM/dd HH:mm:ss"
            val formatter = SimpleDateFormat(pattern)
            val Date1: Date = formatter.parse(date1)
            val Date2: Date = formatter.parse(date2)
            if (Date1 == Date2 || Date1.after(Date2)  ) {
                tvStartDateDetailEvent.text = date2
            } else {
                context?.showAlertDialog(getString(R.string.choose_start_time))
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
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
                tvEndDateDetailEvent.text = date2
            } else {
                context?.showAlertDialog(getString(R.string.choose_end_time))
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun sendDataToFragment(dateTime: String, dateAndTime: String) {
        if (dateTime == "start") {
            if(compareDateNow(dateAndTime) == true) {
                if (tvEndDateDetailEvent.text.toString().isEmpty()) {
                    tvStartDateDetailEvent.text = dateAndTime
                } else {
                    compareDates1(tvEndDateDetailEvent.text.toString(), dateAndTime)
                }
            }
        }
        if (dateTime == "end"){
            compareDates(tvEndDateDetailEvent.text.toString(), dateAndTime)
        }
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

    private  fun onPostEditEvent(){
        dataClient = RetrofitClient.getClient()!!.create(DataClient::class.java)
        val titleEditEvent: String = tvTitleEventDetail.text.toString()
        val startTimeEditEvent: String = tvStartDateDetailEvent.text.toString()
        val endTimeTimeEditEvent: String = tvEndDateDetailEvent.text.toString()
        val speaker: String = tvSpeakerDetail.text.toString()
        val documentLink: String = linkDetail.text.toString()
        val descriptionEditEvent: String = tvDescriptionEventDetail.text.toString()
        val valueEvent= CreateEventModelPostServer(
                "",
                titleEditEvent,
                descriptionEditEvent,
                documentLink,
                speaker,
                startTimeEditEvent,
                endTimeTimeEditEvent)

        val call=dataClient?.editEvent(eventDetail?.id!!,Constant.id,valueEvent)

        call?.enqueue(object :Callback<BaseDataResponseServer<Any>>{
            override fun onFailure(call: Call<BaseDataResponseServer<Any>>?, t: Throwable?) {
            }
            override fun onResponse(call: Call<BaseDataResponseServer<Any>>?, response: Response<BaseDataResponseServer<Any>>?) {
                if(response?.body()?.code == 0){
                    context?.showAlertDialog("Change Event Success")
                    activity?.onBackPressed()
                }
            }
        })
    }

    private fun convertStringToDate(date: String): Date{
         val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH)
        var d: Date? = null
        try {
                d = sdf.parse(date);
        } catch (ex: ParseException) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        return d!!
    }
}
