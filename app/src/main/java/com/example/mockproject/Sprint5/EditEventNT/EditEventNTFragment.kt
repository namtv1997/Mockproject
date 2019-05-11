package com.example.mockproject.Sprint5.EditEventNT


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_edit_event_nt.*
import android.util.Patterns
import android.text.TextUtils
import android.util.Log
import android.webkit.URLUtil
import com.example.mockproject.Sprint5.EditEventNT.view.IpmEditEvent
import com.example.mockproject.Sprint5.calendarScreen.model.EventModel
import com.example.mockproject.Sprint5.createEventScreen.model.CreateEventModelPostServer
import com.example.mockproject.common.Constant
import com.example.mockproject.retrofit2.DataClient
import com.example.mockproject.retrofit2.RetrofitClient
import com.example.mockproject.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import android.view.Gravity
import android.view.WindowManager
import android.graphics.Point
import com.example.mockproject.R


@SuppressLint("ValidFragment")
class EditEventNTFragment(var event : EventModel) : DialogFragment(), IpmEditEvent {

    var dataClient: DataClient? = null
    private var eventId:String?= null

    private fun getData() {
        eventId=event.id
        edtTittleEditEvent.setText(event.title)
        edtDescriptionEditEvent.setText(event.description)
        edtDocumentLinkEditEvent.setText(event.documentLink)
        edtSpeakerEditEvent.setText(event.speakerLink)
        tvStartTimeEditEvent.text=event.startDateTime
        tvEndTimeEditEvent.text=event.endDateTime
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_event_nt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        btnSaveEditEvent.setOnClickListener {
            saveEditEvent()
        }
        btnCancelEditEvent.setOnClickListener {
            dismiss()
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, activity?.intent)
        }
        tvEndTimeEditEvent.setOnClickListener { endTime() }
        tvStartTimeEditEvent.setOnClickListener { startTime() }

        edtTittleEditEvent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tv_over_quantity_EditEvent.visibility = View.GONE
                if (edtTittleEditEvent.text.toString().length > 64) {
                    tv_over_quantity_EditEvent.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edtDescriptionEditEvent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tv_over_quantity_description_EditEvent.visibility = View.GONE
                if (edtTittleEditEvent.text.toString().length > 255) {
                    tv_over_quantity_description_EditEvent.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        addTextChange(edtDescriptionEditEvent, tv_description_empty_EditEvent, tv_description_empty_EditEvent)
        addTextChange(edtSpeakerEditEvent, tv_speaker_empty, tv_speaker_empty)
        addTextChange(tvStartTimeEditEvent, tv_startTime_EditEvent_empty, tv_startTime_EditEvent_empty)
        addTextChange(tvEndTimeEditEvent, tv_endTime_EditEvent_empty, tv_endTime_EditEvent_empty)
        addTextChange(edtTittleEditEvent, tv_titleEditEvent_empty, tv_titleEditEvent_empty)


    }


    override fun sendDataToFragment(dateTime: String, dateAndTime: String) {
        if (dateTime == "start") {
            if (tvEndTimeEditEvent.text.toString().isEmpty()) {
                tvStartTimeEditEvent.text = dateAndTime
            } else {
                compareDates1(tvEndTimeEditEvent.text.toString(), dateAndTime)
            }
        }
        if (dateTime == "end"){
            compareDates(tvStartTimeEditEvent.text.toString(), dateAndTime)

        }
    }

    private fun startTime() {
        val bundle = Bundle()
        bundle.putString("dateTime", "start")
        val customCalendarFragment = CustomCalendarFragment()
        customCalendarFragment.arguments = bundle
        customCalendarFragment.setListener(this)
        addFragment(customCalendarFragment)
    }

    private fun endTime() {
        val bundle = Bundle()
        bundle.putString("dateTime", "end")
        val customCalendarFragment = CustomCalendarFragment()
        customCalendarFragment.arguments = bundle
        customCalendarFragment.setListener(this)
        addFragment(customCalendarFragment)

    }

    private fun saveEditEvent() {
        val titleEditEvent: String = edtTittleEditEvent.text.toString()
        val startTimeEditEvent: String = tvStartTimeEditEvent.text.toString()
        val endTimeTimeEditEvent: String = tvEndTimeEditEvent.text.toString()
        val speaker: String = edtSpeakerEditEvent.text.toString()
        val documentLink: String = edtDocumentLinkEditEvent.text.toString()
        val descriptionEditEvent: String = edtDescriptionEditEvent.text.toString()

        if (titleEditEvent.isEmpty()) {
            tv_titleEditEvent_empty.visibility = View.VISIBLE
        } else if (descriptionEditEvent.isEmpty()) {
            tv_description_empty_EditEvent.visibility = View.VISIBLE
        } else if (speaker.isEmpty()) {
            tv_speaker_empty.visibility = View.VISIBLE
        } else if (startTimeEditEvent.isEmpty()) {
            tv_startTime_EditEvent_empty.visibility = View.VISIBLE
        } else if (endTimeTimeEditEvent.isEmpty()) {
            tv_endTime_EditEvent_empty.visibility = View.VISIBLE
        } else if (!checkURL(documentLink)) {
            context?.showAlertDialog(getString(R.string.link_url))
        }else{
            onPostEditEvent()
            val eventModel = EventModel(event.id,
                    titleEditEvent,false,
                    documentLink,
                    speaker,
                    startTimeEditEvent,
                    "",
                    endTimeTimeEditEvent,
                    descriptionEditEvent)
            val intent = Intent()
            val bundle = Bundle()
            bundle.putSerializable("EventDialog", eventModel)
            intent.putExtras(bundle)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss()
        }
    }

    private fun checkURL(input: CharSequence): Boolean {
        if (TextUtils.isEmpty(input)) {
            return true
        }
        val URL_PATTERN = Patterns.WEB_URL
        var isURL = URL_PATTERN.matcher(input).matches()
        if (!isURL) {
            val urlString = input.toString() + ""
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    URL(urlString)
                    isURL = true
                } catch (e: Exception) {
                }

            }
        }
        return isURL
    }

    @SuppressLint("SimpleDateFormat")
    private fun compareDates(date1: String, date2: String) {
        try {
            val pattern = "yyyy/MM/dd HH:mm:ss"
            val formatter = SimpleDateFormat(pattern)
            val Date1: Date = formatter.parse(date1)
            val Date2: Date = formatter.parse(date2)
            if (Date1 == Date2 || Date1.before(Date2)  ) {
                tvEndTimeEditEvent.text = date2
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
                tvStartTimeEditEvent.text = date2
            } else {
                context?.showAlertDialog(getString(R.string.choose_start_time))
            }
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
    }

    private  fun onPostEditEvent(){
        dataClient = RetrofitClient.getClient()!!.create(DataClient::class.java)
        val titleEditEvent: String = edtTittleEditEvent.text.toString()
        val startTimeEditEvent: String = tvStartTimeEditEvent.text.toString()
        val endTimeTimeEditEvent: String = tvEndTimeEditEvent.text.toString()
        val speaker: String = edtSpeakerEditEvent.text.toString()
        val documentLink: String = edtDocumentLinkEditEvent.text.toString()
        val descriptionEditEvent: String = edtDescriptionEditEvent.text.toString()
        val valueEvent= CreateEventModelPostServer(
                "",
                titleEditEvent,
                descriptionEditEvent,
                documentLink,
                speaker,
                startTimeEditEvent,
                endTimeTimeEditEvent)

        val call=dataClient?.editEvent(eventId!!,Constant.id,valueEvent)

        Log.e("aaa", Constant.id)
        call?.enqueue(object :Callback<BaseDataResponseServer<Any>>{
            override fun onFailure(call: Call<BaseDataResponseServer<Any>>?, t: Throwable?) {
                Log.d("aaa",t?.message.toString())
            }
            override fun onResponse(call: Call<BaseDataResponseServer<Any>>?, response: Response<BaseDataResponseServer<Any>>?) {
                Log.d("aaa",response?.body()?.code.toString())
            }
        })
    }

    override fun onResume() {
        val window = dialog.window
        val size = Point()
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        window.setLayout((size.x * 0.85).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        super.onResume()
    }
}
